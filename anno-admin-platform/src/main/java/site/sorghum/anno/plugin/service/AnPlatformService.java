package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.ao.AnPlatform;

public interface AnPlatformService {
    /**
     * 查询全局平台
     *
     * @return {@link AnPlatform}
     */
    AnPlatform queryGlobalAnPlatform();
}
