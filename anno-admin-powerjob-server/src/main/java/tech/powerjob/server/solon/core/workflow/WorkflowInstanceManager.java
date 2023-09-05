package tech.powerjob.server.solon.core.workflow;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.core.alarm.AlarmCenter;
import tech.powerjob.server.solon.core.alarm.AlarmUtils;
import tech.powerjob.server.solon.core.alarm.module.WorkflowInstanceAlarm;
import tech.powerjob.server.solon.core.helper.StatusMappingHelper;
import tech.powerjob.server.solon.core.lock.UseCacheLock;
import tech.powerjob.server.solon.core.service.WorkflowNodeHandleService;
import tech.powerjob.server.solon.core.uid.IdGenerateService;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAGUtils;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowNodeInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInstanceInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowNodeInfoRepository;
import site.sorghum.anno.pre.plugin.ao.SysUser;
import site.sorghum.anno.pre.plugin.dao.SysUserDao;
import tech.powerjob.common.SystemInstanceResult;
import tech.powerjob.common.WorkflowContextConstant;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.WorkflowInstanceStatus;
import tech.powerjob.common.enums.WorkflowNodeType;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.common.serialize.JsonUtils;
import tech.powerjob.common.utils.CommonUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 管理运行中的工作流实例
 *
 * @author tjq
 * @author Echo009
 * @since 2020/5/26
 */
@Slf4j
@Component
public class WorkflowInstanceManager {

    @Inject
    private AlarmCenter alarmCenter;
    @Inject
    private IdGenerateService idGenerateService;
    @Db
    private JobInfoRepository jobInfoRepository;
    @Db
    private SysUserDao userDao;
    @Db
    private WorkflowInfoRepository workflowInfoRepository;
    @Db
    private WorkflowInstanceInfoRepository workflowInstanceInfoRepository;
    @Db
    private WorkflowNodeInfoRepository workflowNodeInfoRepository;
    @Inject
    private WorkflowNodeHandleService workflowNodeHandleService;

    /**
     * 创建工作流任务实例
     * ********************************************
     * 2021-02-03 modify by Echo009
     * 通过 initParams 初始化工作流上下文（wfContext）
     * ********************************************
     *
     * @param wfInfo            工作流任务元数据（描述信息）
     * @param initParams        启动参数
     * @param expectTriggerTime 预计执行时间
     * @return wfInstanceId
     */
    public String create(WorkflowInfoDO wfInfo, String initParams, Long expectTriggerTime, String parentWfInstanceId) {

        String wfId = wfInfo.getId();
        String wfInstanceId = idGenerateService.allocate();
        // 构造实例信息
        WorkflowInstanceInfoDO newWfInstance = constructWfInstance(wfInfo, initParams, expectTriggerTime, wfId, wfInstanceId);
        if (parentWfInstanceId != null) {
            // 处理子工作流
            newWfInstance.setParentWfInstanceId(parentWfInstanceId);
            // 直接透传上下文
            newWfInstance.setWfContext(initParams);
        }

        PEWorkflowDAG dag = null;
        try {
            dag = JSON.parseObject(wfInfo.getPeDAG(), PEWorkflowDAG.class);
            // 校验 DAG 信息
            if (!WorkflowDAGUtils.valid(dag)) {
                log.error("[Workflow-{}|{}] DAG of this workflow is illegal! maybe you has modified the DAG info directly in database!", wfId, wfInstanceId);
                throw new PowerJobException(SystemInstanceResult.INVALID_DAG);
            }
            // 初始化节点信息
            initNodeInfo(dag);
            //  最后检查工作流中的任务是否均处于可用状态（没有被删除）
            Set<String> allJobIds = Sets.newHashSet();
            dag.getNodes().forEach(node -> {
                if (node.getNodeType() == WorkflowNodeType.JOB.getCode()) {
                    allJobIds.add(String.valueOf(node.getJobId()));
                }
                // 将节点的初始状态置为等待派发
                node.setStatus(InstanceStatus.WAITING_DISPATCH.getV());
            });
            int needNum = allJobIds.size();
            long dbNum = jobInfoRepository.countByAppIdAndStatusInAndIdIn(wfInfo.getAppId(), Sets.newHashSet(SwitchableStatus.ENABLE.getV(), SwitchableStatus.DISABLE.getV()), allJobIds);
            log.debug("[Workflow-{}|{}] contains {} jobs, find {} jobs in database.", wfId, wfInstanceId, needNum, dbNum);
            if (dbNum < allJobIds.size()) {
                log.warn("[Workflow-{}|{}] this workflow need {} jobs, but just find {} jobs in database, maybe you delete or disable some job!", wfId, wfInstanceId, needNum, dbNum);
                throw new PowerJobException(SystemInstanceResult.CAN_NOT_FIND_JOB);
            }
            newWfInstance.setDag(JSON.toJSONString(dag));
            workflowInstanceInfoRepository.insert(newWfInstance);
        } catch (Exception e) {
            if (dag != null) {
                newWfInstance.setDag(JSON.toJSONString(dag));
            }
            handleWfInstanceFinalStatus(newWfInstance, e.getMessage(), WorkflowInstanceStatus.FAILED);
        }
        return wfInstanceId;
    }

    /**
     * 初始化节点信息
     */
    private void initNodeInfo(PEWorkflowDAG dag) {
        for (PEWorkflowDAG.Node node : dag.getNodes()) {
            WorkflowNodeInfoDO workflowNodeInfo = workflowNodeInfoRepository.selectById(node.getNodeId());
            if (workflowNodeInfo == null) {
                throw new PowerJobException(SystemInstanceResult.CAN_NOT_FIND_NODE);
            }
            if (workflowNodeInfo.getType() == null) {
                // 前向兼容
                workflowNodeInfo.setType(WorkflowNodeType.JOB.getCode());
            }
            // 填充基础信息
            node.setNodeType(workflowNodeInfo.getType())
                .setJobId(Long.valueOf(workflowNodeInfo.getJobId()))
                .setNodeName(workflowNodeInfo.getNodeName())
                .setNodeParams(workflowNodeInfo.getNodeParams())
                .setEnable(workflowNodeInfo.getEnable())
                .setSkipWhenFailed(workflowNodeInfo.getSkipWhenFailed());

            // 任务节点，初始化节点参数时需要特殊处理
            if (node.getNodeType() == WorkflowNodeType.JOB.getCode()) {
                // 任务节点缺失任务信息
                if (workflowNodeInfo.getJobId() == null) {
                    throw new PowerJobException(SystemInstanceResult.ILLEGAL_NODE);
                }
                JobInfoDO jobInfo = jobInfoRepository.selectById(workflowNodeInfo.getJobId());
                if (jobInfo == null) {
                    throw new PowerJobException(SystemInstanceResult.CAN_NOT_FIND_JOB);
                }
                if (!StringUtils.isBlank(workflowNodeInfo.getNodeParams())) {
                    node.setNodeParams(workflowNodeInfo.getNodeParams());
                } else {
                    node.setNodeParams(jobInfo.getJobParams());
                }
            }
        }
    }

    /**
     * 构造工作流实例，并初始化基础信息（不包括 DAG ）
     */
    private WorkflowInstanceInfoDO constructWfInstance(WorkflowInfoDO wfInfo, String initParams, Long expectTriggerTime, String wfId, String wfInstanceId) {

        LocalDateTime now = LocalDateTime.now();
        WorkflowInstanceInfoDO newWfInstance = new WorkflowInstanceInfoDO();
        newWfInstance.setAppId(wfInfo.getAppId());
        newWfInstance.setWfInstanceId(wfInstanceId);
        newWfInstance.setWorkflowId(wfId);
        newWfInstance.setStatus(WorkflowInstanceStatus.WAITING.getV());
        newWfInstance.setExpectedTriggerTime(expectTriggerTime);
        newWfInstance.setActualTriggerTime(System.currentTimeMillis());
        newWfInstance.setWfInitParams(initParams);

        // 如果 initParams 是个合法的 Map<String,String> JSON 串则直接将其注入 wfContext
        boolean injectDirect = false;
        try {
            Map<String, String> parseRes = JSON.parseObject(initParams, new TypeReference<Map<String, String>>() {
            });
            if (parseRes != null && !parseRes.isEmpty()) {
                injectDirect = true;
            }
        } catch (Exception e) {
            // ignore
        }
        if (injectDirect) {
            newWfInstance.setWfContext(initParams);
        } else {
            // 初始化上下文
            Map<String, String> wfContextMap = Maps.newHashMap();
            wfContextMap.put(WorkflowContextConstant.CONTEXT_INIT_PARAMS_KEY, initParams);
            newWfInstance.setWfContext(JsonUtils.toJSONString(wfContextMap));
        }
        newWfInstance.setCreateTime(now);
        newWfInstance.setUpdateTime(now);
        return newWfInstance;
    }

    /**
     * 开始任务
     * ********************************************
     * 2021-02-03 modify by Echo009
     * 1、工作流支持配置重复的任务节点
     * 2、移除参数 initParams，改为统一从工作流实例中获取
     * 传递工作流实例的 wfContext 作为 初始启动参数
     * 3、通过 {@link WorkflowDAGUtils#listReadyNodes} 兼容原地重试逻辑
     * ********************************************
     *
     * @param wfInfo       工作流任务信息
     * @param wfInstanceId 工作流任务实例ID
     */
    @UseCacheLock(type = "processWfInstance", key = "#wfInfo.getMaxWfInstanceNum() > 0 ? #wfInfo.getId() : #wfInstanceId", concurrencyLevel = 1024)
    public void start(WorkflowInfoDO wfInfo, String wfInstanceId) {

        WorkflowInstanceInfoDO wfInstanceInfo = workflowInstanceInfoRepository.findByWfInstanceId(wfInstanceId);
        if (wfInstanceInfo == null) {
            log.error("[WorkflowInstanceManager] can't find metadata by workflowInstanceId({}).", wfInstanceId);
            return;
        }

        // 不是等待中，不再继续执行（可能上一流程已经失败）
        if (wfInstanceInfo.getStatus() != WorkflowInstanceStatus.WAITING.getV()) {
            log.info("[Workflow-{}|{}] workflowInstance({}) needn't running any more.", wfInfo.getId(), wfInstanceId, wfInstanceInfo);
            return;
        }
        // 最大实例数量 <= 0 表示不限制
        if (wfInfo.getMaxWfInstanceNum() > 0) {
            // 并发度控制
            long instanceConcurrency = workflowInstanceInfoRepository.countByWorkflowIdAndStatusIn(wfInfo.getId(), WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS);
            if (instanceConcurrency > wfInfo.getMaxWfInstanceNum()) {
                handleWfInstanceFinalStatus(wfInstanceInfo, String.format(SystemInstanceResult.TOO_MANY_INSTANCES, instanceConcurrency, wfInfo.getMaxWfInstanceNum()), WorkflowInstanceStatus.FAILED);
                return;
            }
        }
        try {
            // 从实例中读取工作流信息
            PEWorkflowDAG dag = JSON.parseObject(wfInstanceInfo.getDag(), PEWorkflowDAG.class);
            // 根节点有可能被 disable
            List<PEWorkflowDAG.Node> readyNodes = WorkflowDAGUtils.listReadyNodes(dag);
            // 先处理其中的控制节点
            List<PEWorkflowDAG.Node> controlNodes = findControlNodes(readyNodes);
            while (!controlNodes.isEmpty()) {
                workflowNodeHandleService.handleControlNodes(controlNodes, dag, wfInstanceInfo);
                readyNodes = WorkflowDAGUtils.listReadyNodes(dag);
                controlNodes = findControlNodes(readyNodes);
            }
            if (readyNodes.isEmpty()) {
                // 没有就绪的节点（所有节点都被禁用）
                wfInstanceInfo.setFinishedTime(System.currentTimeMillis());
                wfInstanceInfo.setDag(JSON.toJSONString(dag));
                log.warn("[Workflow-{}|{}] workflowInstance({}) needn't running ", wfInfo.getId(), wfInstanceId, wfInstanceInfo);
                handleWfInstanceFinalStatus(wfInstanceInfo, SystemInstanceResult.NO_ENABLED_NODES, WorkflowInstanceStatus.SUCCEED);
                return;
            }
            // 需要更新工作流实例状态
            wfInstanceInfo.setStatus(WorkflowInstanceStatus.RUNNING.getV());
            // 处理任务节点
            workflowNodeHandleService.handleTaskNodes(readyNodes, dag, wfInstanceInfo);
            log.info("[Workflow-{}|{}] start workflow successfully", wfInfo.getId(), wfInstanceId);
        } catch (Exception e) {
            log.error("[Workflow-{}|{}] start workflow: {} failed.", wfInfo.getId(), wfInstanceId, wfInfo, e);
            handleWfInstanceFinalStatus(wfInstanceInfo, e.getMessage(), WorkflowInstanceStatus.FAILED);
        }
    }


    /**
     * 下一步（当工作流的某个任务完成时调用该方法）
     * ********************************************
     * 2021-02-03 modify by Echo009
     * 1、工作流支持配置重复的任务节点
     * 2、不再获取上游任务的结果作为实例参数而是传递工作流
     * 实例的 wfContext 作为 实例参数
     * 3、通过 {@link WorkflowDAGUtils#listReadyNodes} 支持跳过禁用的节点
     * ********************************************
     *
     * @param wfInstanceId 工作流任务实例ID
     * @param instanceId   具体完成任务的某个任务实例ID
     * @param status       完成任务的任务实例状态（SUCCEED/FAILED/STOPPED）
     * @param result       完成任务的任务实例结果
     */
    @SuppressWarnings({"squid:S3776", "squid:S2142", "squid:S1141"})
    @UseCacheLock(type = "processWfInstance", key = "#wfInstanceId", concurrencyLevel = 1024)
    public void move(String wfInstanceId, String instanceId, InstanceStatus status, String result) {

        WorkflowInstanceInfoDO wfInstance = workflowInstanceInfoRepository.findByWfInstanceId(wfInstanceId);
        if (wfInstance == null) {
            log.error("[WorkflowInstanceManager] can't find metadata by workflowInstanceId({}).", wfInstanceId);
            return;
        }
        String wfId = wfInstance.getWorkflowId();

        // 特殊处理手动终止 且 工作流实例已经不在运行状态的情况
        if (status == InstanceStatus.STOPPED && !WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS.contains(wfInstance.getStatus())) {
            // 由用户手动停止工作流实例导致，不需要任何操作
            return;
        }

        try {
            PEWorkflowDAG dag = JSON.parseObject(wfInstance.getDag(), PEWorkflowDAG.class);
            // 更新完成节点状态
            boolean allFinished = true;
            PEWorkflowDAG.Node instanceNode = null;
            for (PEWorkflowDAG.Node node : dag.getNodes()) {
                if (instanceId.equals(String.valueOf(node.getInstanceId()))) {
                    node.setStatus(status.getV());
                    node.setResult(result);
                    node.setFinishedTime(CommonUtils.formatTime(System.currentTimeMillis()));
                    instanceNode = node;
                    log.info("[Workflow-{}|{}] node(nodeId={},jobId={},instanceId={}) finished in workflowInstance, status={},result={}", wfId, wfInstanceId, node.getNodeId(), node.getJobId(), instanceId, status.name(), result);
                }
                if (InstanceStatus.GENERALIZED_RUNNING_STATUS.contains(node.getStatus())) {
                    allFinished = false;
                }
            }
            if (instanceNode == null) {
                // DAG 中的节点实例已经被覆盖（原地重试，生成了新的实例信息），直接忽略
                log.warn("[Workflow-{}|{}] current job instance(instanceId={}) is dissociative! it will be ignore! ", wfId, wfInstanceId, instanceId);
                return;
            }

            wfInstance.setUpdateTime(LocalDateTime.now());
            wfInstance.setDag(JSON.toJSONString(dag));
            // 工作流已经结束（某个节点失败导致工作流整体已经失败），仅更新最新的 DAG 图
            if (!WorkflowInstanceStatus.GENERALIZED_RUNNING_STATUS.contains(wfInstance.getStatus())) {
                workflowInstanceInfoRepository.insert(wfInstance);
                log.info("[Workflow-{}|{}] workflow already finished(status={}), just update the dag info.", wfId, wfInstanceId, wfInstance.getStatus());
                return;
            }

            // 任务失败 && 不允许失败跳过，DAG 流程被打断，整体失败
            if (status == InstanceStatus.FAILED && WorkflowDAGUtils.isNotAllowSkipWhenFailed(instanceNode)) {
                log.warn("[Workflow-{}|{}] workflow instance process failed because middle task(instanceId={}) failed", wfId, wfInstanceId, instanceId);
                handleWfInstanceFinalStatus(wfInstance, SystemInstanceResult.MIDDLE_JOB_FAILED, WorkflowInstanceStatus.FAILED);
                return;
            }

            // 子任务被手动停止
            if (status == InstanceStatus.STOPPED) {
                handleWfInstanceFinalStatus(wfInstance, SystemInstanceResult.MIDDLE_JOB_STOPPED, WorkflowInstanceStatus.STOPPED);
                log.warn("[Workflow-{}|{}] workflow instance stopped because middle task(instanceId={}) stopped by user", wfId, wfInstanceId, instanceId);
                return;
            }
            // 注意：这里会直接跳过 disable 的节点
            List<PEWorkflowDAG.Node> readyNodes = WorkflowDAGUtils.listReadyNodes(dag);
            // 如果没有就绪的节点，需要再次判断是否已经全部完成
            if (readyNodes.isEmpty() && isFinish(dag)) {
                allFinished = true;
            }
            // 工作流执行完毕（能执行到这里代表该工作流内所有子任务都执行成功了）
            if (allFinished) {
                // 这里得重新更新一下，因为 WorkflowDAGUtils#listReadyNodes 可能会更新节点状态
                wfInstance.setDag(JSON.toJSONString(dag));
                // 最终任务的结果作为整个 workflow 的结果
                handleWfInstanceFinalStatus(wfInstance, result, WorkflowInstanceStatus.SUCCEED);
                log.info("[Workflow-{}|{}] process successfully.", wfId, wfInstanceId);
                return;
            }
            // 先处理其中的控制节点
            List<PEWorkflowDAG.Node> controlNodes = findControlNodes(readyNodes);
            while (!controlNodes.isEmpty()) {
                workflowNodeHandleService.handleControlNodes(controlNodes, dag, wfInstance);
                readyNodes = WorkflowDAGUtils.listReadyNodes(dag);
                controlNodes = findControlNodes(readyNodes);
            }
            // 再次判断是否已完成 （允许控制节点出现在末尾）
            if (readyNodes.isEmpty()) {
                if (isFinish(dag)) {
                    wfInstance.setDag(JSON.toJSONString(dag));
                    handleWfInstanceFinalStatus(wfInstance, result, WorkflowInstanceStatus.SUCCEED);
                    log.info("[Workflow-{}|{}] process successfully.", wfId, wfInstanceId);
                    return;
                }
                // 没有就绪的节点 但 还没执行完成，仅更新 DAG
                wfInstance.setDag(JSON.toJSONString(dag));
                workflowInstanceInfoRepository.insert(wfInstance);
                return;
            }
            // 处理任务节点
            workflowNodeHandleService.handleTaskNodes(readyNodes, dag, wfInstance);
        } catch (Exception e) {
            handleWfInstanceFinalStatus(wfInstance, "MOVE NEXT STEP FAILED: " + e.getMessage(), WorkflowInstanceStatus.FAILED);
            log.error("[Workflow-{}|{}] update failed.", wfId, wfInstanceId, e);
        }

    }

    /**
     * 更新工作流上下文
     * fix : 得和其他操作工作流实例的方法用同一把锁才行，不然有并发问题，会导致节点状态被覆盖
     *
     * @param wfInstanceId          工作流实例
     * @param appendedWfContextData 追加的上下文数据
     * @since 2021/02/05
     */
    @UseCacheLock(type = "processWfInstance", key = "#wfInstanceId", concurrencyLevel = 1024)
    public void updateWorkflowContext(String wfInstanceId, Map<String, String> appendedWfContextData) {

        try {
            WorkflowInstanceInfoDO wfInstance = workflowInstanceInfoRepository.findByWfInstanceId(wfInstanceId);
            if (wfInstance == null) {
                log.error("[WorkflowInstanceManager] can't find metadata by workflowInstanceId({}).", wfInstanceId);
                return;
            }
            HashMap<String, String> wfContext = JSON.parseObject(wfInstance.getWfContext(), new TypeReference<HashMap<String, String>>() {
            });
            for (Map.Entry<String, String> entry : appendedWfContextData.entrySet()) {
                String key = entry.getKey();
                String originValue = wfContext.put(key, entry.getValue());
                log.info("[Workflow-{}|{}] update workflow context {} : {} -> {}", wfInstance.getWorkflowId(), wfInstance.getWfInstanceId(), key, originValue, entry.getValue());
            }
            wfInstance.setWfContext(JSON.toJSONString(wfContext));
            workflowInstanceInfoRepository.insert(wfInstance);

        } catch (Exception e) {
            log.error("[WorkflowInstanceManager] update workflow(workflowInstanceId={}) context failed.", wfInstanceId, e);
        }

    }

    private void handleWfInstanceFinalStatus(WorkflowInstanceInfoDO wfInstance, String result, WorkflowInstanceStatus workflowInstanceStatus) {
        wfInstance.setStatus(workflowInstanceStatus.getV());
        wfInstance.setResult(result);
        wfInstance.setFinishedTime(System.currentTimeMillis());
        wfInstance.setUpdateTime(LocalDateTime.now());
        workflowInstanceInfoRepository.insert(wfInstance);

        // 处理子工作流
        if (wfInstance.getParentWfInstanceId() != null) {
            // 先处理上下文
            if (workflowInstanceStatus == WorkflowInstanceStatus.SUCCEED) {
                HashMap<String, String> wfContext = JSON.parseObject(wfInstance.getWfContext(), new TypeReference<HashMap<String, String>>() {
                });
                Solon.context().getBean(this.getClass()).updateWorkflowContext(wfInstance.getParentWfInstanceId(), wfContext);
            }
            // 处理父工作流, fix https://github.com/PowerJob/PowerJob/issues/465
            Solon.context().getBean(this.getClass()).move(wfInstance.getParentWfInstanceId(), wfInstance.getWfInstanceId(), StatusMappingHelper.toInstanceStatus(workflowInstanceStatus), result);
        }

        // 报警
        if (workflowInstanceStatus == WorkflowInstanceStatus.FAILED) {
            try {
                WorkflowInfoDO wfInfo = workflowInfoRepository.selectById(wfInstance.getWorkflowId());
                if (wfInfo != null) {
                    WorkflowInstanceAlarm content = new WorkflowInstanceAlarm();

                    BeanUtil.copyProperties(wfInfo, content);
                    BeanUtil.copyProperties(wfInstance, content);
                    content.setResult(result);

                    List<SysUser> userList = userDao.selectUserList(wfInfo.getNotifyUserIds());
                    alarmCenter.alarmFailed(content, AlarmUtils.convertUserInfoList2AlarmTargetList(userList));
                }

            } catch (Exception ignore) {
                // ignore
            }
        }
    }


    private List<PEWorkflowDAG.Node> findControlNodes(List<PEWorkflowDAG.Node> readyNodes) {
        return readyNodes.stream().filter(node -> {
            WorkflowNodeType nodeType = WorkflowNodeType.of(node.getNodeType());
            return nodeType.isControlNode();
        }).collect(Collectors.toList());
    }

    private boolean isFinish(PEWorkflowDAG dag) {
        for (PEWorkflowDAG.Node node : dag.getNodes()) {
            if (InstanceStatus.GENERALIZED_RUNNING_STATUS.contains(node.getStatus())) {
                return false;
            }
        }
        return true;
    }

}
