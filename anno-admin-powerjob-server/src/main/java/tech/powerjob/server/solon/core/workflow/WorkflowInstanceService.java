package tech.powerjob.server.solon.core.workflow;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.SystemInstanceResult;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.WorkflowInstanceStatus;
import tech.powerjob.common.enums.WorkflowNodeType;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.common.response.WorkflowInstanceInfoDTO;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.core.lock.UseCacheLock;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAGUtils;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInstanceInfoRepository;
import tech.powerjob.server.solon.remote.server.redirector.DesignateServer;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 工作流实例服务
 *
 * @author tjq
 * @author Echo009
 * @since 2020/5/31
 */
@Slf4j
@Component
public class WorkflowInstanceService {

    @Inject
    private InstanceService instanceService;

    @Db
    private WorkflowInstanceInfoRepository wfInstanceInfoRepository;

    @Inject
    private WorkflowInstanceManager workflowInstanceManager;

    @Db
    private WorkflowInfoRepository workflowInfoRepository;

    /**
     * 停止工作流实例（入口）
     *
     * @param wfInstanceId 工作流实例ID
     * @param appId        所属应用ID
     */
    public void stopWorkflowInstanceEntrance(String wfInstanceId, String appId) {
        WorkflowInstanceInfoDO wfInstance = fetchWfInstance(wfInstanceId, appId);
        if (!WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS.contains(wfInstance.getStatus())) {
            throw new PowerJobException("workflow instance already stopped");
        }
        // 如果这是一个被嵌套的工作流，则终止父工作流
        if (wfInstance.getParentWfInstanceId() != null) {
            Solon.context().getBean(this.getClass()).stopWorkflowInstance(wfInstance.getParentWfInstanceId(), appId);
            return;
        }
        Solon.context().getBean(this.getClass()).stopWorkflowInstance(wfInstanceId, appId);
    }

    /**
     * 停止工作流实例
     *
     * @param wfInstanceId 工作流实例ID
     * @param appId        所属应用ID
     */
    @DesignateServer
    @UseCacheLock(type = "processWfInstance", key = "#wfInstanceId", concurrencyLevel = 1024)
    public void stopWorkflowInstance(String wfInstanceId, String appId) {
        WorkflowInstanceInfoDO wfInstance = fetchWfInstance(wfInstanceId, appId);
        if (!WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS.contains(wfInstance.getStatus())) {
            throw new PowerJobException("workflow instance already stopped");
        }
        // 停止所有已启动且未完成的服务
        PEWorkflowDAG dag = JSON.parseObject(wfInstance.getDag(), PEWorkflowDAG.class);
        // 遍历所有节点，终止正在运行的
        dag.getNodes().forEach(node -> {
            try {
                if (node.getInstanceId() != null && InstanceStatus.GENERALIZED_RUNNING_STATUS.contains(node.getStatus())) {
                    log.debug("[WfInstance-{}] instance({}) is running, try to stop it now.", wfInstanceId, node.getInstanceId());
                    node.setStatus(InstanceStatus.STOPPED.getV());
                    node.setResult(SystemInstanceResult.STOPPED_BY_USER);
                    // 特殊处理嵌套工作流节点
                    if (Objects.equals(node.getNodeType(), WorkflowNodeType.NESTED_WORKFLOW.getCode())) {
                        stopWorkflowInstance(String.valueOf(node.getInstanceId()), appId);
                        //
                    } else {
                        // 注意，这里并不保证一定能终止正在运行的实例
                        instanceService.stopInstance(appId, String.valueOf(node.getInstanceId()));
                    }
                }
            } catch (Exception e) {
                log.warn("[WfInstance-{}] stop instance({}) failed.", wfInstanceId, JSON.toJSONString(node), e);
            }
        });

        // 修改数据库状态
        wfInstance.setDag(JSON.toJSONString(dag));
        wfInstance.setStatus(WorkflowInstanceStatus.STOPPED.getV());
        wfInstance.setResult(SystemInstanceResult.STOPPED_BY_USER);
        wfInstance.setUpdateTime(LocalDateTime.now());
        wfInstanceInfoRepository.insert(wfInstance);

        log.info("[WfInstance-{}] stop workflow instance successfully~", wfInstanceId);
    }

    /**
     * Add by Echo009 on 2021/02/07
     *
     * @param wfInstanceId 工作流实例ID
     * @param appId        应用ID
     */
    @DesignateServer
    @UseCacheLock(type = "processWfInstance", key = "#wfInstanceId", concurrencyLevel = 1024)
    public void retryWorkflowInstance(String wfInstanceId, String appId) {
        WorkflowInstanceInfoDO wfInstance = fetchWfInstance(wfInstanceId, appId);
        // 仅允许重试 失败的工作流
        if (WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS.contains(wfInstance.getStatus())) {
            throw new PowerJobException("workflow instance is running");
        }
        if (wfInstance.getStatus() == WorkflowInstanceStatus.SUCCEED.getV()) {
            throw new PowerJobException("workflow instance is already successful");
        }
        // 因为 DAG 非法 或者 因任务信息缺失 而失败的工作流实例无法重试
        if (SystemInstanceResult.CAN_NOT_FIND_JOB.equals(wfInstance.getResult())) {
            throw new PowerJobException("you can't retry the workflow instance which is missing job info!");
        }
        // 校验 DAG 信息
        PEWorkflowDAG dag;
        try {
            dag = JSON.parseObject(wfInstance.getDag(), PEWorkflowDAG.class);
            if (!WorkflowDAGUtils.valid(dag)) {
                throw new PowerJobException(SystemInstanceResult.INVALID_DAG);
            }
        } catch (Exception e) {
            throw new PowerJobException("you can't retry the workflow instance whose DAG is illegal!");
        }
        // 检查当前工作流信息
        WorkflowInfoDO workflowInfo = workflowInfoRepository.selectById(wfInstance.getWorkflowId());
        if (workflowInfo == null || workflowInfo.getStatus() == SwitchableStatus.DISABLE.getV()) {
            throw new PowerJobException("you can't retry the workflow instance whose metadata is unavailable!");
        }
        WorkflowDAGUtils.resetRetryableNode(dag);
        wfInstance.setDag(JSON.toJSONString(dag));
        // 更新工作流实例状态，不覆盖实际触发时间
        wfInstance.setStatus(WorkflowInstanceStatus.WAITING.getV());
        wfInstance.setUpdateTime(LocalDateTime.now());
        wfInstanceInfoRepository.insert(wfInstance);
        // 立即开始
        workflowInstanceManager.start(workflowInfo, wfInstanceId);
    }


    public WorkflowInstanceInfoDTO fetchWorkflowInstanceInfo(String wfInstanceId, String appId) {
        WorkflowInstanceInfoDO wfInstance = fetchWfInstance(wfInstanceId, appId);
        WorkflowInstanceInfoDTO dto = new WorkflowInstanceInfoDTO();
        BeanUtil.copyProperties(wfInstance, dto);
        return dto;
    }

    public WorkflowInstanceInfoDO fetchWfInstance(String wfInstanceId, String appId) {
        WorkflowInstanceInfoDO wfInstance = wfInstanceInfoRepository.findByWfInstanceId(wfInstanceId);
        if (wfInstance == null) {
            throw new IllegalArgumentException("can't find workflow instance by wfInstanceId: " + wfInstanceId);
        }
        if (!Objects.equals(appId, wfInstance.getAppId())) {
            throw new PowerJobException("Permission Denied!");
        }
        return wfInstance;
    }

    /**
     * Add by Echo009 on 2021/02/20
     * 将节点标记成功
     * 注意：这里仅能标记真正执行失败的且不允许跳过的节点
     * 即处于 [失败且不允许跳过] 的节点
     * 而且仅会操作工作流实例 DAG 中的节点信息（状态、result）
     * 并不会改变对应任务实例中的任何信息
     * <p>
     * 还是加把锁保平安 ~
     *
     * @param wfInstanceId 工作流实例 ID
     * @param nodeId       节点 ID
     */
    @DesignateServer
    @UseCacheLock(type = "processWfInstance", key = "#wfInstanceId", concurrencyLevel = 1024)
    public void markNodeAsSuccess(String appId, String wfInstanceId, Long nodeId) {

        WorkflowInstanceInfoDO wfInstance = fetchWfInstance(wfInstanceId, appId);
        // 校验工作流实例状态，运行中的不允许处理
        if (WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS.contains(wfInstance.getStatus())) {
            throw new PowerJobException("you can't mark the node in a running workflow!");
        }
        // 这里一定能反序列化成功
        PEWorkflowDAG dag = JSON.parseObject(wfInstance.getDag(), PEWorkflowDAG.class);
        PEWorkflowDAG.Node targetNode = null;
        for (PEWorkflowDAG.Node node : dag.getNodes()) {
            if (node.getNodeId().equals(nodeId)) {
                targetNode = node;
                break;
            }
        }
        if (targetNode == null) {
            throw new PowerJobException("can't find the node in current DAG!");
        }
        boolean allowSkipWhenFailed = targetNode.getSkipWhenFailed() != null && targetNode.getSkipWhenFailed();
        // 仅允许处理 执行失败的且不允许失败跳过的节点
        if (targetNode.getInstanceId() != null
            && targetNode.getStatus() == InstanceStatus.FAILED.getV()
            // 不允许失败跳过
            && !allowSkipWhenFailed) {
            // 仅处理工作流实例中的节点信息
            targetNode.setStatus(InstanceStatus.SUCCEED.getV())
                .setResult(SystemInstanceResult.MARK_AS_SUCCESSFUL_NODE);

            wfInstance.setDag(JSON.toJSONString(dag));
            wfInstanceInfoRepository.insert(wfInstance);
            return;
        }
        // 其他情况均拒绝处理
        throw new PowerJobException("you can only mark the node which is failed and not allow to skip!");

    }

}
