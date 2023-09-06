package site.sorghum.anno.pre.suppose.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.SneakyThrows;
import org.noear.wood.IPage;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.service.impl.DbServiceWood;

import java.util.List;

@Getter
@Named
public class VirtualJoinTableProxy<T> implements AnnoPreBaseProxy<T> {

    @Inject
    DbServiceWood dbServiceWood;

    @Override
    public void beforeFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @SneakyThrows
    @Override
    public void afterFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<T> page) {
        IPage<T> virtualIPage = dbServiceWood.page(tClass, dbConditions, pageParam);
        page.setTotal(virtualIPage.getTotal());
        page.setList(virtualIPage.getList());
        page.setList(virtualIPage.getList());
    }

    @Override
    public void beforeAdd(T data) {

    }

    @Override
    public void afterAdd(T data) {

    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, T data) {

    }

    @Override
    public void afterUpdate(T data) {

    }

    @Override
    public void beforeDelete(Class<T> tClass, List<DbCondition> dbConditions) {
    }

    @Override
    public void afterDelete(Class<T> tClass, List<DbCondition> dbConditions) {

    }


}
