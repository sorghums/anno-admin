package site.sorghum.anno.plugin.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno.plugin.ao.AnPlatform;
import site.sorghum.anno.plugin.dao.AnPlatformDao;
import site.sorghum.anno.plugin.service.AnPlatformService;

import java.util.List;

@Named
public class AnPlatformServiceImpl implements AnPlatformService {
    @Inject
    AnPlatformDao anPlatformDao;

    @Override
    public AnPlatform queryGlobalAnPlatform(){
        List<AnPlatform> platforms = anPlatformDao.list();
        if (!platforms.isEmpty()){
            return platforms.get(0);
        }
        return null;
    }
}
