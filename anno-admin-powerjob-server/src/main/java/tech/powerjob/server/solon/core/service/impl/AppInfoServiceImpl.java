package tech.powerjob.server.solon.core.service.impl;

import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.core.service.AppInfoService;
import tech.powerjob.server.solon.persistence.remote.model.AppInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.AppInfoRepository;
import tech.powerjob.common.exception.PowerJobException;

import java.util.Objects;

/**
 * AppInfoServiceImpl
 *
 * @author tjq
 * @since 2023/3/4
 */
@Component
public class AppInfoServiceImpl implements AppInfoService {

    @Db
    private AppInfoRepository appInfoRepository;

    /**
     * 验证应用访问权限
     * @param appName 应用名称
     * @param password 密码
     * @return 应用ID
     */
    @Override
    public String assertApp(String appName, String password) {

        AppInfoDO appInfo = appInfoRepository.findByAppName(appName).orElseThrow(() -> new PowerJobException("can't find appInfo by appName: " + appName));
        if (Objects.equals(appInfo.getPassword(), password)) {
            return appInfo.getId();
        }
        throw new PowerJobException("password error!");
    }
}
