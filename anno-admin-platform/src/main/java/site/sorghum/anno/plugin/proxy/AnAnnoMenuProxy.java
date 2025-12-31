package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbOrderBy;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.ArrayList;

/**
 * 菜单代理
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@Named
public class AnAnnoMenuProxy implements AnnoBaseProxy<AnAnnoMenu> {

    @Override
    public void beforeAdd(AnAnnoMenu data) {
        String parseData = null;
        // ------ 解析菜单解析 ------
        if (StrUtil.isNotBlank(data.getParseData())) {
            parseData = data.getParseData().trim();
        }
        data.setParseData(parseData);
        // 默认Sort设置
        if (data.getSort() == null){
            data.setSort(0);
        }
    }

    @Override
    public void beforeUpdate(AnAnnoMenu data, DbCriteria criteria) {
        beforeAdd(data);
    }

    @Override
    public void beforeFetch(DbCriteria criteria) {
        DbOrderBy order = new DbOrderBy();
        order.setOrderByItems(new ArrayList<>());
        if (criteria.getOrder() == null || !CollUtil.isNotEmpty(order.getOrderByItems())) {
            order.getOrderByItems().add(new DbOrderBy.OrderByItem("desc", "sort"));
            order.getOrderByItems().add(new DbOrderBy.OrderByItem("desc", "id"));
            criteria.setOrder(
                order
            );
        }

        AnnoBaseProxy.super.beforeFetch(criteria);
    }
}
