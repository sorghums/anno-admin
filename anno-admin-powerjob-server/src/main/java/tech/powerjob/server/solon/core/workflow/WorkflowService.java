package tech.powerjob.server.solon.core.workflow;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.common.SJ;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.common.timewheel.holder.InstanceTimeWheelService;
import tech.powerjob.server.solon.core.scheduler.TimingStrategyService;
import tech.powerjob.server.solon.core.service.NodeValidateService;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAG;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAGUtils;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowNodeInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowNodeInfoRepository;
import tech.powerjob.server.solon.remote.server.redirector.DesignateServer;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.model.LifeCycle;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.common.request.http.SaveWorkflowNodeRequest;
import tech.powerjob.common.request.http.SaveWorkflowRequest;
import tech.powerjob.common.utils.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Workflow 服务
 *
 * @author tjq
 * @author zenggonggu
 * @author Echo009
 * @since 2020/5/26
 */
@Slf4j
@Component
public class WorkflowService {

    @Inject
    private WorkflowInstanceManager workflowInstanceManager;
    @Db
    private WorkflowInfoRepository workflowInfoRepository;
    @Db
    private WorkflowNodeInfoRepository workflowNodeInfoRepository;
    @Inject
    private NodeValidateService nodeValidateService;
    @Inject
    private TimingStrategyService timingStrategyService;

    /**
     * 保存/修改工作流信息
     * <p>
     * 注意这里不会保存 DAG 信息
     *
     * @param req 请求
     * @return 工作流ID
     */
    @Tran
    public Long saveWorkflow(SaveWorkflowRequest req) {

        req.valid();

        Long wfId = req.getId();
        WorkflowInfoDO wf;
        if (wfId == null) {
            wf = new WorkflowInfoDO();
            wf.setUpdateTime(LocalDateTime.now());
        } else {
            Long finalWfId = wfId;
            wf = workflowInfoRepository.findById(String.valueOf(wfId)).orElseThrow(() -> new IllegalArgumentException("can't find workflow by id:" + finalWfId));
        }

        BeanUtil.copyProperties(req, wf);
        wf.setUpdateTime(LocalDateTime.now());
        wf.setStatus(req.isEnable() ? SwitchableStatus.ENABLE.getV() : SwitchableStatus.DISABLE.getV());
        wf.setTimeExpressionType(req.getTimeExpressionType().getV());

        if (req.getNotifyUserIds() != null) {
            wf.setNotifyUserIds(SJ.COMMA_JOINER.join(req.getNotifyUserIds()));
        }
        if (req.getLifeCycle() != null) {
            wf.setLifecycle(JSON.toJSONString(req.getLifeCycle()));
        }
        if (TimeExpressionType.FREQUENT_TYPES.contains(req.getTimeExpressionType().getV())) {
            // 固定频率类型的任务不计算
            wf.setTimeExpression(null);
        } else {
            LifeCycle lifeCycle = Optional.ofNullable(req.getLifeCycle()).orElse(LifeCycle.EMPTY_LIFE_CYCLE);
            Long nextValidTime = timingStrategyService.calculateNextTriggerTimeWithInspection(TimeExpressionType.of(wf.getTimeExpressionType()), wf.getTimeExpression(), lifeCycle.getStart(), lifeCycle.getEnd());
            wf.setNextTriggerTime(nextValidTime);
        }
        // 新增工作流，需要先 save 一下获取 ID
        if (wfId == null) {
            workflowInfoRepository.insert(wf);
            wfId = Long.valueOf(wf.getId());
        }
        wf.setPeDAG(validateAndConvert2String(String.valueOf(wfId), req.getDag()));
        workflowInfoRepository.insert(wf);
        return wfId;
    }

    /**
     * 保存 DAG 信息
     * 这里会物理删除游离的节点信息
     */
    private String validateAndConvert2String(String wfId, PEWorkflowDAG dag) {
        if (dag == null || !WorkflowDAGUtils.valid(dag)) {
            throw new PowerJobException("illegal DAG");
        }
        // 注意：这里只会保存图相关的基础信息，nodeId,jobId,jobName(nodeAlias)
        // 其中 jobId，jobName 均以节点中的信息为准
        List<String> nodeIdList = Lists.newArrayList();
        List<PEWorkflowDAG.Node> newNodes = Lists.newArrayList();
        WorkflowDAG complexDag = WorkflowDAGUtils.convert(dag);
        for (PEWorkflowDAG.Node node : dag.getNodes()) {
            WorkflowNodeInfoDO nodeInfo = workflowNodeInfoRepository.findById(node.getNodeId()).orElseThrow(() -> new PowerJobException("can't find node info by id :" + node.getNodeId()));
            // 更新工作流 ID
            if (nodeInfo.getWorkflowId() == null) {
                nodeInfo.setWorkflowId(String.valueOf(wfId));
                nodeInfo.setUpdateTime(LocalDateTime.now());
                workflowNodeInfoRepository.insert(nodeInfo);
            }
            if (!wfId.equals(nodeInfo.getWorkflowId())) {
                throw new PowerJobException("can't use another workflow's node");
            }
            nodeValidateService.complexValidate(nodeInfo, complexDag);
            // 只保存节点的 ID 信息，清空其他信息
            newNodes.add(new PEWorkflowDAG.Node(node.getNodeId()));
            nodeIdList.add(String.valueOf(node.getNodeId()));
        }
        dag.setNodes(newNodes);
        int deleteCount = workflowNodeInfoRepository.deleteByWorkflowIdAndIdNotIn(wfId, nodeIdList);
        log.warn("[WorkflowService-{}] delete {} dissociative nodes of workflow", wfId, deleteCount);
        return JSON.toJSONString(dag);
    }


    /**
     * 深度复制工作流
     *
     * @param wfId  工作流 ID
     * @param appId APP ID
     * @return 生成的工作流 ID
     */
    @Tran
    public String copyWorkflow(String wfId, String appId) {

        WorkflowInfoDO originWorkflow = permissionCheck(wfId, appId);
        if (originWorkflow.getStatus() == SwitchableStatus.DELETED.getV()) {
            throw new IllegalStateException("can't copy the workflow which has been deleted!");
        }
        // 拷贝基础信息
        WorkflowInfoDO copyWorkflow = new WorkflowInfoDO();
        BeanUtil.copyProperties(originWorkflow, copyWorkflow);
        copyWorkflow.setId(null);
        copyWorkflow.setUpdateTime(LocalDateTime.now());
        copyWorkflow.setUpdateTime(LocalDateTime.now());
        copyWorkflow.setWfName(copyWorkflow.getWfName() + "_COPY");
        // 先 save 获取 id
        workflowInfoRepository.insert(copyWorkflow);

        if (StringUtils.isEmpty(copyWorkflow.getPeDAG())) {
            return copyWorkflow.getId();
        }

        PEWorkflowDAG dag = JSON.parseObject(copyWorkflow.getPeDAG(), PEWorkflowDAG.class);

        // 拷贝节点信息，并且更新 DAG 中的节点信息
        if (!CollectionUtils.isEmpty(dag.getNodes())) {
            // originNodeId => copyNodeId
            HashMap<Long, Long> nodeIdMap = new HashMap<>(dag.getNodes().size(), 1);
            // 校正 节点信息
            for (PEWorkflowDAG.Node node : dag.getNodes()) {

                WorkflowNodeInfoDO originNode = workflowNodeInfoRepository.findById(node.getNodeId()).orElseThrow(() -> new IllegalArgumentException("can't find workflow Node by id: " + node.getNodeId()));

                WorkflowNodeInfoDO copyNode = new WorkflowNodeInfoDO();
                BeanUtil.copyProperties(originNode, copyNode);
                copyNode.setId(null);
                copyNode.setWorkflowId(copyWorkflow.getId());
                copyNode.setUpdateTime(LocalDateTime.now());
                copyNode.setUpdateTime(LocalDateTime.now());

                workflowNodeInfoRepository.insert(copyNode);
                nodeIdMap.put(Long.valueOf(originNode.getId()), Long.valueOf(copyNode.getId()));

                node.setNodeId(Long.valueOf(copyNode.getId()));
            }
            // 校正 边信息
            for (PEWorkflowDAG.Edge edge : dag.getEdges()) {
                edge.setFrom(nodeIdMap.get(edge.getFrom()));
                edge.setTo(nodeIdMap.get(edge.getTo()));
            }
        }
        copyWorkflow.setPeDAG(JSON.toJSONString(dag));
        workflowInfoRepository.insert(copyWorkflow);
        return copyWorkflow.getId();
    }


    /**
     * 获取工作流元信息，这里获取到的 DAG 包含节点的完整信息（是否启用、是否允许失败跳过）
     *
     * @param wfId  工作流ID
     * @param appId 应用ID
     * @return 对外输出对象
     */
    public WorkflowInfoDO fetchWorkflow(String wfId, String appId) {
        WorkflowInfoDO wfInfo = permissionCheck(wfId, appId);
        fillWorkflow(wfInfo);
        return wfInfo;
    }

    /**
     * 删除工作流（软删除）
     *
     * @param wfId  工作流ID
     * @param appId 所属应用ID
     */
    public void deleteWorkflow(String wfId, String appId) {
        WorkflowInfoDO wfInfo = permissionCheck(wfId, appId);
        wfInfo.setStatus(SwitchableStatus.DELETED.getV());
        wfInfo.setUpdateTime(LocalDateTime.now());
        workflowInfoRepository.updateById(wfInfo);
    }

    /**
     * 禁用工作流
     *
     * @param wfId  工作流ID
     * @param appId 所属应用ID
     */
    public void disableWorkflow(String wfId, String appId) {
        WorkflowInfoDO wfInfo = permissionCheck(wfId, appId);
        wfInfo.setStatus(SwitchableStatus.DISABLE.getV());
        wfInfo.setUpdateTime(LocalDateTime.now());
        workflowInfoRepository.updateById(wfInfo);
    }

    /**
     * 启用工作流
     *
     * @param wfId  工作流ID
     * @param appId 所属应用ID
     */
    public void enableWorkflow(String wfId, String appId) {
        WorkflowInfoDO wfInfo = permissionCheck(wfId, appId);
        wfInfo.setStatus(SwitchableStatus.ENABLE.getV());
        wfInfo.setUpdateTime(LocalDateTime.now());
        workflowInfoRepository.updateById(wfInfo);
    }

    /**
     * 立即运行工作流
     *
     * @param wfId       工作流ID
     * @param appId      所属应用ID
     * @param initParams 启动参数
     * @param delay      延迟时间
     * @return 该 workflow 实例的 instanceId（wfInstanceId）
     */
    @DesignateServer
    public String runWorkflow(String wfId, String appId, String initParams, Long delay) {

        delay = delay == null ? 0 : delay;
        WorkflowInfoDO wfInfo = permissionCheck(wfId, appId);

        log.info("[WorkflowService-{}] try to run workflow, initParams={},delay={} ms.", wfInfo.getId(), initParams, delay);
        String wfInstanceId = workflowInstanceManager.create(wfInfo, initParams, System.currentTimeMillis() + delay, null);
        if (delay <= 0) {
            workflowInstanceManager.start(wfInfo, wfInstanceId);
        } else {
            InstanceTimeWheelService.schedule(wfInstanceId, delay, () -> workflowInstanceManager.start(wfInfo, wfInstanceId));
        }
        return wfInstanceId;
    }


    /**
     * 保存工作流节点（新增 或者 保存）
     *
     * @param workflowNodeRequestList 工作流节点
     * @return 更新 或者 创建后的工作流节点信息
     */
    @Tran
    public List<WorkflowNodeInfoDO> saveWorkflowNode(List<SaveWorkflowNodeRequest> workflowNodeRequestList) {
        if (CollectionUtils.isEmpty(workflowNodeRequestList)) {
            return Collections.emptyList();
        }
        final Long appId = workflowNodeRequestList.get(0).getAppId();
        List<WorkflowNodeInfoDO> res = new ArrayList<>(workflowNodeRequestList.size());
        for (SaveWorkflowNodeRequest req : workflowNodeRequestList) {
            req.valid();
            // 必须位于同一个 APP 下
            if (!appId.equals(req.getAppId())) {
                throw new PowerJobException("node list must are in the same app");
            }
            WorkflowNodeInfoDO workflowNodeInfo;
            if (req.getId() != null) {
                workflowNodeInfo = workflowNodeInfoRepository.findById(req.getId()).orElseThrow(() -> new IllegalArgumentException("can't find workflow Node by id: " + req.getId()));
            } else {
                workflowNodeInfo = new WorkflowNodeInfoDO();
                workflowNodeInfo.setUpdateTime(LocalDateTime.now());
            }
            BeanUtil.copyProperties(req, workflowNodeInfo);
            workflowNodeInfo.setType(req.getType());
            nodeValidateService.simpleValidate(workflowNodeInfo);
            workflowNodeInfo.setUpdateTime(LocalDateTime.now());
            workflowNodeInfoRepository.saveOrUpdate(workflowNodeInfo);
            res.add(workflowNodeInfo);
        }
        return res;
    }


    private void fillWorkflow(WorkflowInfoDO wfInfo) {

        PEWorkflowDAG dagInfo = null;
        try {
            dagInfo = JSON.parseObject(wfInfo.getPeDAG(), PEWorkflowDAG.class);
        } catch (Exception e) {
            log.warn("[WorkflowService-{}]illegal DAG : {}", wfInfo.getId(), wfInfo.getPeDAG());
        }
        if (dagInfo == null) {
            return;
        }

        Map<String, WorkflowNodeInfoDO> nodeIdNodInfoMap = Maps.newHashMap();

        workflowNodeInfoRepository.findByWorkflowId(wfInfo.getId()).forEach(
            e -> nodeIdNodInfoMap.put(e.getId(), e)
        );
        // 填充节点信息
        if (!CollectionUtils.isEmpty(dagInfo.getNodes())) {
            for (PEWorkflowDAG.Node node : dagInfo.getNodes()) {
                WorkflowNodeInfoDO nodeInfo = nodeIdNodInfoMap.get(String.valueOf(node.getNodeId()));
                if (nodeInfo != null) {
                    node.setNodeType(nodeInfo.getType())
                        .setJobId(Long.valueOf(nodeInfo.getJobId()))
                        .setEnable(nodeInfo.getEnable())
                        .setSkipWhenFailed(nodeInfo.getSkipWhenFailed())
                        .setNodeName(nodeInfo.getNodeName())
                        .setNodeParams(nodeInfo.getNodeParams());
                }
            }
        }
        wfInfo.setPeDAG(JSON.toJSONString(dagInfo));
    }


    private WorkflowInfoDO permissionCheck(String wfId, String appId) {
        WorkflowInfoDO wfInfo = workflowInfoRepository.findById(wfId).orElseThrow(() -> new IllegalArgumentException("can't find workflow by id: " + wfId));
        if (!wfInfo.getAppId().equals(appId)) {
            throw new PowerJobException("Permission Denied! can't operate other app's workflow!");
        }
        return wfInfo;
    }
}
