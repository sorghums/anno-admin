package site.sorghum.anno.test.modular.ebusiness;

import org.noear.solon.annotation.Component;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;

import java.util.List;

/**
 * 电商商品虚拟表
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Component
@Slf4j
public class BusinessVirtualTableProxy implements AnnoBaseProxy<BusinessVirtualTable> {
    @Db
    DbContext dbContext;

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(BusinessVirtualTable.class)
        };
    }

    @Override
    public void beforeAdd(BusinessVirtualTable data) {
        log.info("beforeAdd: {}", data);
    }

    @Override
    public void afterAdd(BusinessVirtualTable data) {
        log.info("afterAdd: {}", data);
    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, BusinessVirtualTable data) {
        log.info("beforeUpdate: {}", data);
    }

    @Override
    public void afterUpdate(BusinessVirtualTable data) {
        log.info("afterUpdate: {}", data);
    }

    @Override
    public void beforeDelete(Class<BusinessVirtualTable> tClass, List<DbCondition> dbConditions) {
        log.info("beforeDelete: {}", dbConditions);
    }

    @Override
    public void afterDelete(Class<BusinessVirtualTable> tClass, List<DbCondition> dbConditions) {
        log.info("afterDelete: {}", dbConditions);
    }

    @Override
    public void beforeFetch(Class<BusinessVirtualTable> tClass, List<DbCondition> dbConditions, PageParam pageParam) {
        log.info("beforeFetch: {}", dbConditions);
    }

    @SneakyThrows
    @Override
    public void afterFetch(Class<BusinessVirtualTable> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<BusinessVirtualTable> page) {
        log.info("afterFetch: {}", page);
        // 自定义复杂SQL查询
        List<BusinessVirtualTable> businessProduct = dbContext.table("business_product").selectList("*", BusinessVirtualTable.class);
        page.getList().addAll(businessProduct);

    }
}
