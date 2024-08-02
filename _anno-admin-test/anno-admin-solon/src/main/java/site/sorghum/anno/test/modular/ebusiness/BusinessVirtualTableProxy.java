package site.sorghum.anno.test.modular.ebusiness;

import jakarta.inject.Named;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;

import java.util.List;

/**
 * 电商商品虚拟表
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Named
@Slf4j
public class BusinessVirtualTableProxy implements AnnoBaseProxy<BusinessVirtualTable> {
    @Db
    DbContext dbContext;

    @Override
    public String[] supportEntities() {
        return AnnoBaseProxy.super.supportEntities();
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
    public void beforeUpdate(BusinessVirtualTable data, DbCriteria criteria) {
        log.info("beforeUpdate: {}", data);
    }

    @Override
    public void afterUpdate(BusinessVirtualTable data) {
        log.info("afterUpdate: {}", data);
    }

    @Override
    public void beforeDelete(DbCriteria criteria) {
        log.info("beforeDelete: {}", criteria);
    }

    @Override
    public void afterDelete(DbCriteria criteria) {
        log.info("afterDelete: {}", criteria);
    }

    @Override
    public void beforeFetch(DbCriteria criteria) {
        log.info("beforeFetch: {}", criteria);
    }

    @SneakyThrows
    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<BusinessVirtualTable> page) {
        log.info("afterFetch: {}", page);
        // 自定义复杂SQL查询
        List<BusinessVirtualTable> businessProduct = dbContext.table("business_product").selectList("*", BusinessVirtualTable.class);
        page.getList().addAll(businessProduct);

    }
}
