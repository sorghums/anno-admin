package site.sorghum.anno.suppose.proxy;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import lombok.Getter;
import lombok.SneakyThrows;
import org.noear.wood.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.service.impl.DbServiceWood;

import java.util.List;

@Getter
@Component
@org.springframework.stereotype.Component
public class VirtualJoinTableProxy<T> implements AnnoBaseProxy<T> {

    @Inject
    @Autowired
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
