package site.sorghum.anno.pre.suppose.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.noear.wood.IPage;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.impl.DbServiceWood;

import java.util.List;

@Named
public class VirtualJoinTableProxy<T> implements AnnoPreBaseProxy<T> {

    @Inject
    DbServiceWood dbServiceWood;

    @Override
    public void beforeFetch(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @SneakyThrows
    @Override
    public void afterFetch(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<T> page) {
        IPage<T> virtualIPage = dbServiceWood.page(tableParam, dbConditions, pageParam);
        page.setTotal(virtualIPage.getTotal());
        page.setList(virtualIPage.getList());
        page.setList(virtualIPage.getList());
    }

    @Override
    public void beforeAdd(TableParam<T> tableParam, T data) {

    }

    @Override
    public void afterAdd(T data) {

    }

    @Override
    public void beforeUpdate(TableParam<T> tableParam, List<DbCondition> dbConditions, T data) {

    }

    @Override
    public void afterUpdate(T data) {

    }

    @Override
    public void beforeDelete(TableParam<T> tableParam, List<DbCondition> dbConditions) {
        throw new BizException("连表信息不允许删除");
    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }


}
