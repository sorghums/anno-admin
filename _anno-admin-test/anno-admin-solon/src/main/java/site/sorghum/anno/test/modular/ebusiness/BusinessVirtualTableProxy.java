package site.sorghum.anno.test.modular.ebusiness;

import jakarta.inject.Named;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

import java.util.Collection;
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
    @Override
    public void beforeAdd(TableParam<BusinessVirtualTable> tableParam, BusinessVirtualTable data) {
        log.info("beforeAdd: {}", data);
    }

    @Override
    public void afterAdd(BusinessVirtualTable data) {
        log.info("afterAdd: {}", data);
    }

    @Override
    public void beforeUpdate(TableParam<BusinessVirtualTable> tableParam, List<DbCondition> dbConditions, BusinessVirtualTable data) {
        log.info("beforeUpdate: {}", data);
    }

    @Override
    public void afterUpdate(BusinessVirtualTable data) {
        log.info("afterUpdate: {}", data);
    }

    @Override
    public void beforeDelete(TableParam<BusinessVirtualTable> tableParam, List<DbCondition> dbConditions) {
        log.info("beforeDelete: {}", dbConditions);
    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {
        log.info("afterDelete: {}", dbConditions);
    }

    @Override
    public void beforeFetch(TableParam<BusinessVirtualTable> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {
        log.info("beforeFetch: {}", dbConditions);
    }

    @Override
    public void afterFetch(AnnoPage<BusinessVirtualTable> page) {
        log.info("afterFetch: {}", page);
        List<BusinessVirtualTable> list = page.getList();
        list.add(
            new BusinessVirtualTable(){{
                setProductName("测试商品");
            }}
        );
    }
}
