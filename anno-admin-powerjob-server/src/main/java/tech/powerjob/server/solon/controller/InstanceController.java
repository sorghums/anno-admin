package tech.powerjob.server.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.response.ResultDTO;
import tech.powerjob.server.solon.common.utils.OmsFileUtils;
import tech.powerjob.server.solon.controller.response.InstanceDetailVO;
import tech.powerjob.server.solon.core.instance.InstanceLogService;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.core.service.CacheService;
import tech.powerjob.server.solon.persistence.StringPage;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;

import java.io.File;
import java.net.URL;


/**
 * 任务实例 Controller
 *
 * @author tjq
 * @since 2020/4/9
 */
@SaIgnore
@Slf4j
@Controller
@Mapping("/powerjob/instance")
public class InstanceController {


    @Inject
    private InstanceService instanceService;
    @Inject
    private InstanceLogService instanceLogService;

    @Inject
    private CacheService cacheService;
    @Db
    private InstanceInfoRepository instanceInfoRepository;

    @Mapping(value = "/stop", method = MethodType.GET)
    public ResultDTO<Void> stopInstance(String appId, String instanceId) {
        instanceService.stopInstance(appId, instanceId);
        return ResultDTO.success(null);
    }

    @Mapping(value = "/retry", method = MethodType.GET)
    public ResultDTO<Void> retryInstance(String appId, String instanceId) {
        instanceService.retryInstance(appId, instanceId);
        return ResultDTO.success(null);
    }

    @Mapping(value = "/detail", method = MethodType.GET)
    public ResultDTO<InstanceDetailVO> getInstanceDetail(String instanceId) {
        return ResultDTO.success(InstanceDetailVO.from(instanceService.getInstanceDetail(instanceId)));
    }

    @Mapping(value = "/log", method = MethodType.GET)
    public ResultDTO<StringPage> getInstanceLog(String appId, String instanceId, Long index) {
        return ResultDTO.success(instanceLogService.fetchInstanceLog(appId, instanceId, index - 1));
    }

    @Mapping(value = "/downloadLogUrl", method = MethodType.GET)
    public ResultDTO<String> getDownloadUrl(Long appId, Long instanceId) {
        return ResultDTO.success(instanceLogService.fetchDownloadUrl(appId, instanceId));
    }

    @Mapping(value = "/downloadLog", method = MethodType.GET)
    public void downloadLogFile(String instanceId, Context context) throws Exception {

        File file = instanceLogService.downloadInstanceLog(instanceId);
        context.outputAsFile(file);
    }

    @SneakyThrows
    @Mapping(value = "/downloadLog4Console", method = MethodType.GET)
    public void downloadLog4Console(Long appId, Long instanceId, Context context) {
        // 获取内部下载链接
        String downloadUrl = instanceLogService.fetchDownloadUrl(appId, instanceId);
        // 先下载到本机
        String logFilePath = OmsFileUtils.genTemporaryWorkPath() + String.format("powerjob-%s-%s.log", appId, instanceId);
        File logFile = new File(logFilePath);

        try {
            FileUtils.copyURLToFile(new URL(downloadUrl), logFile);

            // 再推送到浏览器
            context.outputAsFile(logFile);
        } finally {
            FileUtils.forceDelete(logFile);
        }
    }

}
