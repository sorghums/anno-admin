package site.sorghum.anno.test.modular.wtf;

import lombok.SneakyThrows;
import org.noear.solon.annotation.Component;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;

import java.util.List;

@Component
public class WtfABCVirtualProxy implements AnnoBaseProxy<WtfABCVirtual> {

    @Db
    DbContext dbContext;

    @Override
    public void beforeFetch(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @SneakyThrows
    @Override
    public void afterFetch(AnnoPage<WtfABCVirtual> page) {
        List<WtfABCVirtual> wtfABCVirtuals = dbContext.table("wtf_c as t1")
            .leftJoin("wtf_b as t2").on("t1.wtf_b = t2.id")
            .leftJoin("wtf_a as t3").on("t2.wtf_a = t3.id").selectList("*", WtfABCVirtual.class);
        page.setList(wtfABCVirtuals);
    }

    @Override
    public void beforeAdd(TableParam<WtfABCVirtual> tableParam, WtfABCVirtual data) {

    }

    @Override
    public void afterAdd(WtfABCVirtual data) {

    }

    @Override
    public void beforeUpdate(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions, WtfABCVirtual data) {

    }

    @Override
    public void afterUpdate(WtfABCVirtual data) {

    }

    @Override
    public void beforeDelete(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }


}
