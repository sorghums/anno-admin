package site.sorghum.anno.spring.config;

import org.noear.wood.DbContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.plugin.dao.AnAnnoMenuDao;
import site.sorghum.anno.plugin.dao.AnPermissionDao;
import site.sorghum.anno.plugin.dao.AnRoleDao;
import site.sorghum.anno.plugin.dao.SysUserDao;

/**
 * dao配置
 *
 * @author Sorghum
 * @since 2024/01/11
 */
@Configuration
public class DaoConfiguration {

    @Bean
    public AnAnnoMenuDao anAnnoMenuDao(DbContext dbContext){
        return dbContext.mapper(AnAnnoMenuDao.class);
    }

    @Bean
    AnPermissionDao anPermissionDao(DbContext dbContext){
        return dbContext.mapper(AnPermissionDao.class);
    }

    @Bean
    AnRoleDao anRoleDao(DbContext dbContext){
        return dbContext.mapper(AnRoleDao.class);
    }

    @Bean
    SysUserDao sysUserDao(DbContext dbContext){
        return dbContext.mapper(SysUserDao.class);
    }
}
