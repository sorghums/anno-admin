package tech.powerjob.server.solon.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.MethodType;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.OmsConstant;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.response.ResultDTO;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.common.module.WorkerInfo;
import tech.powerjob.server.solon.controller.response.WorkerStatusVO;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;
import tech.powerjob.server.solon.remote.server.self.ServerInfoService;
import tech.powerjob.server.solon.remote.worker.WorkerClusterQueryService;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * 系统信息控制器（服务于前端首页）
 *
 * @author tjq
 * @since 2020/4/14
 */
@Slf4j
@Controller
@Mapping("/pj-system")
public class SystemInfoController {

  @Db
  private JobInfoRepository jobInfoRepository;
  @Db
  private InstanceInfoRepository instanceInfoRepository;
  @Inject
  private ServerInfoService serverInfoService;
  @Inject
  private WorkerClusterQueryService workerClusterQueryService;

  @Mapping(value = "/listWorker", method = MethodType.GET)
  public ResultDTO<List<WorkerStatusVO>> listWorker(String appId) {

    List<WorkerInfo> workerInfos = workerClusterQueryService.getAllWorkers(appId);
    return ResultDTO.success(workerInfos.stream().map(WorkerStatusVO::new).collect(Collectors.toList()));
  }

  @Mapping(value = "/overview", method = MethodType.GET)
  public ResultDTO<SystemOverviewVO> getSystemOverview(String appId) {

    SystemOverviewVO overview = new SystemOverviewVO();

    // 总任务数量
    overview.setJobCount(jobInfoRepository.countByAppIdAndStatusNot(appId, SwitchableStatus.DELETED.getV()));
    // 运行任务数
    overview.setRunningInstanceCount(instanceInfoRepository.countByAppIdAndStatus(appId, InstanceStatus.RUNNING.getV()));
    // 近期失败任务数（24H内）
    Date date = DateUtils.addDays(new Date(), -1);
    overview.setFailedInstanceCount(instanceInfoRepository.countByAppIdAndStatusAndGmtCreateAfter(appId, InstanceStatus.FAILED.getV(), date));

    // 服务器时区
    overview.setTimezone(TimeZone.getDefault().getDisplayName());
    // 服务器时间
    overview.setServerTime(DateFormatUtils.format(new Date(), OmsConstant.TIME_PATTERN));

    overview.setServerInfo(serverInfoService.fetchServiceInfo());

    return ResultDTO.success(overview);
  }

}
