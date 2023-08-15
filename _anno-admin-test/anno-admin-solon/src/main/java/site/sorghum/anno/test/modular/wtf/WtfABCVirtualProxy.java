package site.sorghum.anno.test.modular.wtf;

import lombok.SneakyThrows;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.suppose.proxy.VirtualJoinTableProxy;

import java.util.List;

@Component
public class WtfABCVirtualProxy extends VirtualJoinTableProxy<WtfABCVirtual> {

    @SneakyThrows
    @Override
    public void afterFetch(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<WtfABCVirtual> page) {
        super.afterFetch(tableParam, dbConditions, pageParam, page);
    }

    @Override
    public void beforeDelete(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions) {
        super.beforeDelete(tableParam, dbConditions);
    }


}
