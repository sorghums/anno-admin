package tech.powerjob.server.solon.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.MethodType;
import tech.powerjob.common.OpenAPIConstant;
import tech.powerjob.common.PowerQuery;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.request.http.SaveJobInfoRequest;
import tech.powerjob.common.request.http.SaveWorkflowNodeRequest;
import tech.powerjob.common.request.http.SaveWorkflowRequest;
import tech.powerjob.common.request.query.JobInfoQuery;
import tech.powerjob.common.response.InstanceInfoDTO;
import tech.powerjob.common.response.JobInfoDTO;
import tech.powerjob.common.response.ResultDTO;
import tech.powerjob.common.response.WorkflowInstanceInfoDTO;
import tech.powerjob.server.solon.controller.response.WorkflowInfoVO;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.core.service.AppInfoService;
import tech.powerjob.server.solon.core.service.CacheService;
import tech.powerjob.server.solon.core.service.JobService;
import tech.powerjob.server.solon.core.workflow.WorkflowInstanceService;
import tech.powerjob.server.solon.core.workflow.WorkflowService;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowNodeInfoDO;

import java.util.List;

/**
 * 开放接口（OpenAPI）控制器，对接 oms-client
 *
 * @author tjq
 * @since 2020/4/15
 */
@Controller
@Mapping(OpenAPIConstant.WEB_PATH)
@RequiredArgsConstructor
public class OpenAPIController {

    private final AppInfoService appInfoService;

    private final JobService jobService;

    private final InstanceService instanceService;

    private final WorkflowService workflowService;

    private final WorkflowInstanceService workflowInstanceService;

    private final CacheService cacheService;


    @Mapping(value = OpenAPIConstant.ASSERT, method = MethodType.POST)
    public ResultDTO<Long> assertAppName(String appName, String password) {
        return ResultDTO.success(Long.parseLong(appInfoService.assertApp(appName, password)));
    }

    /* ************* Job 区 ************* */

    @Mapping(value = OpenAPIConstant.SAVE_JOB, method = MethodType.POST)
    public ResultDTO<Long> saveJob(SaveJobInfoRequest request) {
        if (request.getId() != null) {
            checkJobIdValid(request.getId(), request.getAppId());
        }
        return ResultDTO.success(Long.parseLong(jobService.saveJob(request)));
    }

    @Mapping(value = OpenAPIConstant.COPY_JOB, method = MethodType.POST)
    public ResultDTO<Long> copyJob(Long jobId) {
        return ResultDTO.success(Long.parseLong(jobService.copyJob(String.valueOf(jobId)).getId()));
    }

    @Mapping(value = OpenAPIConstant.EXPORT_JOB, method = MethodType.POST)
    public ResultDTO<SaveJobInfoRequest> exportJob(Long jobId, Long appId) {
        checkJobIdValid(jobId, appId);
        return ResultDTO.success(jobService.exportJob(String.valueOf(jobId)));
    }

    @Mapping(value = OpenAPIConstant.FETCH_JOB, method = MethodType.POST)
    public ResultDTO<JobInfoDTO> fetchJob(Long jobId, Long appId) {
        checkJobIdValid(jobId, appId);
        return ResultDTO.success(jobService.fetchJob(String.valueOf(jobId)));
    }

    @Mapping(value = OpenAPIConstant.FETCH_ALL_JOB, method = MethodType.POST)
    public ResultDTO<List<JobInfoDTO>> fetchAllJob(Long appId) {
        return ResultDTO.success(jobService.fetchAllJob(String.valueOf(appId)));
    }

    @Mapping(value = OpenAPIConstant.QUERY_JOB, method = MethodType.POST)
    public ResultDTO<List<JobInfoDTO>> queryJob(JobInfoQuery powerQuery) {
        return ResultDTO.success(jobService.queryJob(powerQuery));
    }

    @Mapping(value = OpenAPIConstant.DELETE_JOB, method = MethodType.POST)
    public ResultDTO<Void> deleteJob(Long jobId, Long appId) {
        checkJobIdValid(jobId, appId);
        jobService.deleteJob(String.valueOf(jobId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.DISABLE_JOB, method = MethodType.POST)
    public ResultDTO<Void> disableJob(Long jobId, Long appId) {
        checkJobIdValid(jobId, appId);
        jobService.disableJob(String.valueOf(jobId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.ENABLE_JOB, method = MethodType.POST)
    public ResultDTO<Void> enableJob(Long jobId, Long appId) {
        checkJobIdValid(jobId, appId);
        jobService.enableJob(String.valueOf(jobId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.RUN_JOB, method = MethodType.POST)
    public ResultDTO<Long> runJob(Long appId, Long jobId, String instanceParams, Long delay) {
        checkJobIdValid(jobId, appId);
        String instanceId = jobService.runJob(String.valueOf(appId), String.valueOf(jobId), instanceParams, delay);
        return ResultDTO.success(Long.parseLong(instanceId));
    }

    /* ************* Instance 区 ************* */

    @Mapping(value = OpenAPIConstant.STOP_INSTANCE, method = MethodType.POST)
    public ResultDTO<Void> stopInstance(Long instanceId, Long appId) {
        checkInstanceIdValid(instanceId, appId);
        instanceService.stopInstance(String.valueOf(appId), String.valueOf(instanceId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.CANCEL_INSTANCE, method = MethodType.POST)
    public ResultDTO<Void> cancelInstance(Long instanceId, Long appId) {
        checkInstanceIdValid(instanceId, appId);
        instanceService.cancelInstance(String.valueOf(appId), String.valueOf(instanceId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.RETRY_INSTANCE, method = MethodType.POST)
    public ResultDTO<Void> retryInstance(Long instanceId, Long appId) {
        checkInstanceIdValid(instanceId, appId);
        instanceService.retryInstance(String.valueOf(appId), String.valueOf(instanceId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.FETCH_INSTANCE_STATUS, method = MethodType.POST)
    public ResultDTO<Integer> fetchInstanceStatus(Long instanceId) {
        InstanceStatus instanceStatus = instanceService.getInstanceStatus(String.valueOf(instanceId));
        return ResultDTO.success(instanceStatus.getV());
    }

    @Mapping(value = OpenAPIConstant.FETCH_INSTANCE_INFO, method = MethodType.POST)
    public ResultDTO<InstanceInfoDTO> fetchInstanceInfo(Long instanceId) {
        return ResultDTO.success(instanceService.getInstanceInfo(String.valueOf(instanceId)));
    }

    @Mapping(value = OpenAPIConstant.QUERY_INSTANCE, method = MethodType.POST)
    public ResultDTO<List<InstanceInfoDTO>> queryInstance(PowerQuery powerQuery) {
        return ResultDTO.success(instanceService.queryInstanceInfo(powerQuery));
    }

    /* ************* Workflow 区 ************* */

    @Mapping(value = OpenAPIConstant.SAVE_WORKFLOW, method = MethodType.POST)
    public ResultDTO<Long> saveWorkflow(SaveWorkflowRequest request) {
        return ResultDTO.success(workflowService.saveWorkflow(request));
    }

    @Mapping(value = OpenAPIConstant.COPY_WORKFLOW, method = MethodType.POST)
    public ResultDTO<Long> copy(Long workflowId, Long appId) {
        String id = workflowService.copyWorkflow(String.valueOf(workflowId), String.valueOf(appId));
        return ResultDTO.success(Long.parseLong(id));
    }


    @Mapping(value = OpenAPIConstant.FETCH_WORKFLOW, method = MethodType.POST)
    public ResultDTO<WorkflowInfoVO> fetchWorkflow(Long workflowId, Long appId) {
        WorkflowInfoDO workflowInfoDO = workflowService.fetchWorkflow(String.valueOf(workflowId), String.valueOf(appId));
        return ResultDTO.success(WorkflowInfoVO.from(workflowInfoDO));
    }

    @Mapping(value = OpenAPIConstant.DELETE_WORKFLOW, method = MethodType.POST)
    public ResultDTO<Void> deleteWorkflow(Long workflowId, Long appId) {
        workflowService.deleteWorkflow(String.valueOf(workflowId), String.valueOf(appId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.DISABLE_WORKFLOW, method = MethodType.POST)
    public ResultDTO<Void> disableWorkflow(Long workflowId, Long appId) {
        workflowService.disableWorkflow(String.valueOf(workflowId), String.valueOf(appId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.ENABLE_WORKFLOW, method = MethodType.POST)
    public ResultDTO<Void> enableWorkflow(Long workflowId, Long appId) {
        workflowService.enableWorkflow(String.valueOf(workflowId), String.valueOf(appId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.RUN_WORKFLOW, method = MethodType.POST)
    public ResultDTO<Long> runWorkflow(Long workflowId, Long appId, String initParams, Long delay) {
        String wfInstanceId = workflowService.runWorkflow(String.valueOf(workflowId), String.valueOf(appId), initParams, delay);
        return ResultDTO.success(Long.parseLong(wfInstanceId));
    }

    @Mapping(value = OpenAPIConstant.SAVE_WORKFLOW_NODE, method = MethodType.POST)
    public ResultDTO<List<WorkflowNodeInfoDO>> saveWorkflowNode(List<SaveWorkflowNodeRequest> request) {
        return ResultDTO.success(workflowService.saveWorkflowNode(request));
    }

    /* ************* Workflow Instance 区 ************* */

    @Mapping(value = OpenAPIConstant.STOP_WORKFLOW_INSTANCE, method = MethodType.POST)
    public ResultDTO<Void> stopWorkflowInstance(Long wfInstanceId, Long appId) {
        workflowInstanceService.stopWorkflowInstanceEntrance(String.valueOf(wfInstanceId), String.valueOf(appId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.RETRY_WORKFLOW_INSTANCE, method = MethodType.POST)
    public ResultDTO<Void> retryWorkflowInstance(Long wfInstanceId, Long appId) {
        workflowInstanceService.retryWorkflowInstance(String.valueOf(wfInstanceId), String.valueOf(appId));
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.MARK_WORKFLOW_NODE_AS_SUCCESS, method = MethodType.POST)
    public ResultDTO<Void> markWorkflowNodeAsSuccess(Long wfInstanceId, Long nodeId, Long appId) {
        workflowInstanceService.markNodeAsSuccess(String.valueOf(appId), String.valueOf(wfInstanceId), nodeId);
        return ResultDTO.success(null);
    }

    @Mapping(value = OpenAPIConstant.FETCH_WORKFLOW_INSTANCE_INFO, method = MethodType.POST)
    public ResultDTO<WorkflowInstanceInfoDTO> fetchWorkflowInstanceInfo(Long wfInstanceId, Long appId) {
        return ResultDTO.success(workflowInstanceService.fetchWorkflowInstanceInfo(String.valueOf(wfInstanceId), String.valueOf(appId)));
    }

    private void checkInstanceIdValid(Long instanceId, Long appId) {
        String realAppId = cacheService.getAppIdByInstanceId(String.valueOf(instanceId));
        if (realAppId == null) {
            throw new IllegalArgumentException("can't find instance by instanceId: " + instanceId);
        }
        if (StrUtil.equals(realAppId, String.valueOf(appId))) {
            return;
        }
        throw new IllegalArgumentException("instance is not belong to the app whose appId is " + appId);
    }

    private void checkJobIdValid(Long jobId, Long appId) {
        String realAppId = cacheService.getAppIdByJobId(String.valueOf(jobId));
        // 查不到，说明 jobId 不存在
        if (realAppId == null) {
            throw new IllegalArgumentException("can't find job by jobId: " + jobId);
        }
        // 不等，说明该job不属于该app，无权限操作
        if (!StrUtil.equals(realAppId, String.valueOf(appId))) {
            throw new IllegalArgumentException("this job is not belong to the app whose appId is " + appId);
        }
    }
}
