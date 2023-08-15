package site.sorghum.anno.test.modular.wtf;

import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.impl.DbServiceWood;
import site.sorghum.anno.pre.suppose.proxy.VirtualJoinTableProxy;

import java.lang.reflect.Field;
import java.util.List;

@Component
@Slf4j
public class WtfABCVirtualProxy extends VirtualJoinTableProxy<WtfABCVirtual> {

    @Inject
    MetadataManager metadataManager;


    @SneakyThrows
    @Override
    public void afterFetch(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<WtfABCVirtual> page) {
        // 系统仅支持查询，其余如新增、修改、删除等操作请自行实现。
        super.afterFetch(tableParam, dbConditions, pageParam, page);
    }

    @SneakyThrows
    @Override
    public void beforeDelete(TableParam<WtfABCVirtual> tableParam, List<DbCondition> dbConditions) {
        log.info("before delete:{}", dbConditions);
        for (DbCondition dbCondition : dbConditions) {
            String field = dbCondition.getField();
            // 根据SQL Column获取对应的Field
            Field fieldBySqlColumn = AnnoFieldCache.getFieldBySqlColumn(WtfABCVirtual.class, field);
        }
        // 查询要删除的数据原信息
        WtfABCVirtual wtfABCVirtual = getDbServiceWood().queryOne(tableParam, dbConditions);
        log.info("before delete:{}", wtfABCVirtual);
        super.beforeDelete(tableParam, dbConditions);
    }
}
