package tech.powerjob.server.solon.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import tech.powerjob.server.solon.core.instance.InstanceLogService;
import tech.powerjob.server.solon.core.instance.InstanceManager;
import tech.powerjob.server.solon.core.workflow.WorkflowInstanceManager;
import tech.powerjob.server.solon.moniter.events.w2s.TtReportInstanceStatusEvent;
import tech.powerjob.server.solon.moniter.events.w2s.WorkerHeartbeatEvent;
import tech.powerjob.server.solon.moniter.events.w2s.WorkerLogReportEvent;
import tech.powerjob.server.solon.remote.worker.WorkerClusterManagerService;
import tech.powerjob.common.RemoteConstant;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.request.TaskTrackerReportInstanceStatusReq;
import tech.powerjob.common.request.WorkerHeartbeat;
import tech.powerjob.common.request.WorkerLogReportReq;
import tech.powerjob.common.response.AskResponse;
import tech.powerjob.common.utils.CollectionUtils;
import tech.powerjob.remote.framework.actor.Actor;

/**
 * receive and process worker's request
 *
 * @author tjq
 * @since 2022/9/11
 */
@Slf4j
@Component
@Actor(path = RemoteConstant.S4W_PATH)
public class WorkerRequestHandlerImpl extends AbWorkerRequestHandler {

    @Inject
    private InstanceManager instanceManager;
    @Inject
    private WorkflowInstanceManager workflowInstanceManager;
    @Inject
    private InstanceLogService instanceLogService;


    @Override
    protected void processWorkerHeartbeat0(WorkerHeartbeat heartbeat, WorkerHeartbeatEvent event) {
        WorkerClusterManagerService.updateStatus(heartbeat);
    }

    @Override
    protected AskResponse processTaskTrackerReportInstanceStatus0(TaskTrackerReportInstanceStatusReq req, TtReportInstanceStatusEvent event) throws Exception {
        // 2021/02/05 如果是工作流中的实例先尝试更新上下文信息，再更新实例状态，这里一定不会有异常
        if (req.getWfInstanceId() != null && !CollectionUtils.isEmpty(req.getAppendedWfContext())) {
            // 更新工作流上下文信息
            workflowInstanceManager.updateWorkflowContext(String.valueOf(req.getWfInstanceId()), req.getAppendedWfContext());
        }

        instanceManager.updateStatus(req);

        // 结束状态（成功/失败）需要回复消息
        if (InstanceStatus.FINISHED_STATUS.contains(req.getInstanceStatus())) {
            return AskResponse.succeed(null);
        }

        return null;
    }

    @Override
    protected void processWorkerLogReport0(WorkerLogReportReq req, WorkerLogReportEvent event) {
        // 这个效率应该不会拉垮吧...也就是一些判断 + Map#get 吧...
        instanceLogService.submitLogs(req.getWorkerAddress(), req.getInstanceLogContents());
    }
}
