package tech.powerjob.server.solon.core.workflow.hanlder.impl;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.SystemInstanceResult;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.WorkflowInstanceStatus;
import tech.powerjob.common.enums.WorkflowNodeType;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.common.utils.CommonUtils;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.core.workflow.WorkflowInstanceManager;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAGUtils;
import tech.powerjob.server.solon.core.workflow.hanlder.TaskNodeHandler;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInstanceInfoRepository;

import java.time.LocalDateTime;

/**
 * @author Echo009
 * @since 2021/12/13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class NestedWorkflowNodeHandler implements TaskNodeHandler {

    @Db
    private WorkflowInfoRepository workflowInfoRepository;

    @Db
    private WorkflowInstanceInfoRepository workflowInstanceInfoRepository;

    @Override
    public void createTaskInstance(PEWorkflowDAG.Node node, PEWorkflowDAG dag, WorkflowInstanceInfoDO wfInstanceInfo) {
        // check
        Long wfId = node.getJobId();
        WorkflowInfoDO targetWf = workflowInfoRepository.findById(wfId).orElse(null);
        if (targetWf == null || targetWf.getStatus() == SwitchableStatus.DELETED.getV()) {
            if (targetWf == null) {
                log.error("[Workflow-{}|{}] invalid nested workflow node({}),target workflow({}) is not exist!", wfInstanceInfo.getWorkflowId(), wfInstanceInfo.getWfInstanceId(), node.getNodeId(), node.getJobId());
            } else {
                log.error("[Workflow-{}|{}] invalid nested workflow node({}),target workflow({}) has been deleted!", wfInstanceInfo.getWorkflowId(), wfInstanceInfo.getWfInstanceId(), node.getNodeId(), node.getJobId());
            }
            throw new PowerJobException("invalid nested workflow node," + node.getNodeId());
        }
        if (node.getInstanceId() != null) {
            // 处理重试的情形，不需要创建实例，仅需要更改对应实例的状态，以及相应的节点状态
            WorkflowInstanceInfoDO wfInstance = workflowInstanceInfoRepository.findByWfInstanceId(String.valueOf(node.getInstanceId()));
            if (wfInstance == null) {
                log.error("[Workflow-{}|{}] invalid nested workflow node({}),target workflow instance({}) is not exist!", wfInstanceInfo.getWorkflowId(), wfInstanceInfo.getWfInstanceId(), node.getNodeId(), node.getInstanceId());
                throw new PowerJobException("invalid nested workflow instance id " + node.getInstanceId());
            }
            // 不用考虑状态，只有失败的工作流嵌套节点状态会被重置
            // 需要将子工作流中失败的节点状态重置为 等待 派发
            try {
                PEWorkflowDAG nodeDag = JSON.parseObject(wfInstance.getDag(), PEWorkflowDAG.class);
                if (!WorkflowDAGUtils.valid(nodeDag)) {
                    throw new PowerJobException(SystemInstanceResult.INVALID_DAG);
                }
                WorkflowDAGUtils.resetRetryableNode(nodeDag);
                wfInstance.setDag(JSON.toJSONString(nodeDag));
                wfInstance.setStatus(WorkflowInstanceStatus.WAITING.getV());
                wfInstance.setUpdateTime(LocalDateTime.now());
                workflowInstanceInfoRepository.insert(wfInstance);
            } catch (Exception e) {
                log.error("[Workflow-{}|{}] invalid nested workflow node({}),target workflow instance({})'s DAG is illegal!", wfInstanceInfo.getWorkflowId(), wfInstanceInfo.getWfInstanceId(), node.getNodeId(), node.getInstanceId(), e);
                throw new PowerJobException("illegal nested workflow instance, id : " + node.getInstanceId());
            }
        } else {
            // 透传当前的上下文创建新的工作流实例
            String wfContext = wfInstanceInfo.getWfContext();
            String instanceId = Solon.context().getBean(WorkflowInstanceManager.class).create(targetWf, wfContext, System.currentTimeMillis(), wfInstanceInfo.getWfInstanceId());
            node.setInstanceId(Long.parseLong(instanceId));
        }
        node.setStartTime(CommonUtils.formatTime(System.currentTimeMillis()));
        node.setStatus(InstanceStatus.RUNNING.getV());
    }

    @Override
    public void startTaskInstance(PEWorkflowDAG.Node node) {
        Long wfId = node.getJobId();
        WorkflowInfoDO targetWf = workflowInfoRepository.findById(wfId).orElse(null);
        Solon.context().getBean(WorkflowInstanceManager.class).start(targetWf, String.valueOf(node.getInstanceId()));
    }

    @Override
    public WorkflowNodeType matchingType() {
        return WorkflowNodeType.NESTED_WORKFLOW;
    }
}
