package tech.powerjob.server.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.core.handle.MethodType;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.request.ServerDiscoveryRequest;
import tech.powerjob.common.response.ResultDTO;
import tech.powerjob.common.utils.CommonUtils;
import tech.powerjob.common.utils.NetUtils;
import tech.powerjob.server.solon.common.aware.ServerInfoAware;
import tech.powerjob.server.solon.common.module.ServerInfo;
import tech.powerjob.server.solon.persistence.remote.model.AppInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.AppInfoRepository;
import tech.powerjob.server.solon.remote.server.election.ServerElectionService;
import tech.powerjob.server.solon.remote.transporter.TransportService;
import tech.powerjob.server.solon.remote.worker.WorkerClusterQueryService;

import java.util.Optional;
import java.util.TimeZone;

/**
 * 处理Worker请求的 Controller
 * Worker启动时，先请求assert验证appName的可用性，再根据得到的appId获取Server地址
 *
 * @author tjq
 * @since 2020/4/4
 */
@SaIgnore
@Controller
@Mapping("/server")
public class ServerController implements ServerInfoAware {

  private ServerInfo serverInfo;

  @Inject
  private TransportService transportService;
  @Inject
  private ServerElectionService serverElectionService;
  @Db
  private AppInfoRepository appInfoRepository;
  @Inject
  private WorkerClusterQueryService workerClusterQueryService;

  @Mapping(value = "/assert", method = MethodType.GET)
  public ResultDTO<Long> assertAppName(String appName) {
    Optional<AppInfoDO> appInfoOpt = appInfoRepository.findByAppName(appName);
    return appInfoOpt.map(appInfoDO -> ResultDTO.success(Long.parseLong(appInfoDO.getId()))).
        orElseGet(() -> ResultDTO.failed(String.format("app(%s) is not registered! Please register the app in oms-console first.", appName)));
  }

  @Mapping(value = "/acquire", method = MethodType.GET)
  public ResultDTO<String> acquireServer(ServerDiscoveryRequest request) {
    return ResultDTO.success(serverElectionService.elect(request));
  }

  @Mapping(value = "/hello", method = MethodType.GET)
  public ResultDTO<JSONObject> ping(@Param(defaultValue = "false") boolean debug) {
    JSONObject res = new JSONObject();
    res.put("localHost", NetUtils.getLocalHost());
    res.put("serverInfo", serverInfo);
    res.put("serverTime", CommonUtils.formatTime(System.currentTimeMillis()));
    res.put("serverTimeTs", System.currentTimeMillis());
    res.put("serverTimeZone", TimeZone.getDefault().getDisplayName());
    res.put("appIds", workerClusterQueryService.getAppId2ClusterStatus().keySet());
    if (debug) {
      res.put("appId2ClusterInfo", JSON.parseObject(JSON.toJSONString(workerClusterQueryService.getAppId2ClusterStatus())));
    }

    try {
      res.put("defaultAddress", JSONObject.toJSONString(transportService.defaultProtocol()));
    } catch (Exception ignore) {
    }

    return ResultDTO.success(res);
  }

  @Override
  public void setServerInfo(ServerInfo serverInfo) {
    this.serverInfo = serverInfo;
  }
}
