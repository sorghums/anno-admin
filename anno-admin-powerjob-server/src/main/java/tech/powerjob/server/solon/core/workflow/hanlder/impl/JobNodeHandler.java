package tech.powerjob.server.solon.core.workflow.hanlder.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.enums.WorkflowNodeType;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.server.solon.core.DispatchService;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.core.workflow.hanlder.TaskNodeHandler;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;

import java.util.Optional;

/**
 * @author Echo009
 * @since 2021/12/9
 */
@Slf4j
@Component
public class JobNodeHandler implements TaskNodeHandler {

    @Db
    private JobInfoRepository jobInfoRepository;

    @Override
    public void createTaskInstance(PEWorkflowDAG.Node node, PEWorkflowDAG dag, WorkflowInstanceInfoDO wfInstanceInfo) {
        // instanceParam 传递的是工作流实例的 wfContext
        String instanceId = Solon.context().getBean(InstanceService.class).create(String.valueOf(node.getJobId()), wfInstanceInfo.getAppId(),
            node.getNodeParams(), wfInstanceInfo.getWfContext(), String.valueOf(wfInstanceInfo.getWfInstanceId()), System.currentTimeMillis()).getInstanceId();
        node.setInstanceId(Long.parseLong(instanceId));
        node.setStatus(InstanceStatus.RUNNING.getV());
        log.info("[Workflow-{}|{}] create readyNode(JOB) instance(nodeId={},jobId={},instanceId={}) successfully~", wfInstanceInfo.getWorkflowId(), wfInstanceInfo.getWfInstanceId(), node.getNodeId(), node.getJobId(), instanceId);
    }

    @Override
    public void startTaskInstance(PEWorkflowDAG.Node node) {
        JobInfoDO jobInfo = jobInfoRepository.findById(String.valueOf(node.getJobId())).orElseGet(JobInfoDO::new);
        // 洗去时间表达式类型
        jobInfo.setTimeExpressionType(TimeExpressionType.WORKFLOW.getV());
        Solon.context().getBean(DispatchService.class).dispatch(jobInfo, String.valueOf(node.getInstanceId()), Optional.empty(), Optional.empty());
    }

    @Override
    public WorkflowNodeType matchingType() {
        return WorkflowNodeType.JOB;
    }
}
