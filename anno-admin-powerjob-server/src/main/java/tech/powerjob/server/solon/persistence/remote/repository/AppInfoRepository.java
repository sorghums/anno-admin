package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.IPage;
import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.db.param.PageParam;
import tech.powerjob.server.solon.persistence.remote.model.AppInfoDO;
import site.sorghum.anno.pre.suppose.mapper.AnnoBaseMapper;

import java.util.List;
import java.util.Optional;

/**
 * AppInfo 数据访问层
 *
 * @author tjq
 * @since 2020/4/1
 */
@Namespace("powerjob.server")
public interface AppInfoRepository extends AnnoBaseMapper<AppInfoDO> {

    default Optional<AppInfoDO> findByAppName(String appName) {
        return Optional.ofNullable(selectItem(m -> m.whereEq("app_name", appName)));
    }

    default IPage<AppInfoDO> findByAppNameLike(String condition, PageParam page) {
        return selectPage(page.getOffset(), page.getPageSize(), m -> m.whereLk("app_name", condition));
    }

    /**
     * 根据 currentServer 查询 appId
     * 其实只需要 id，处于性能考虑可以直接写SQL只返回ID
     */
    @Sql("select * from pj_app_info where current_server = ?")
    List<AppInfoDO> findAllByCurrentServer(String currentServer);

    @Sql(value = "select id from pj_app_info where current_server = ?")
    List<String> listAppIdByCurrentServer(String currentServer);

}
