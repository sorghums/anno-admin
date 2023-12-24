package site.sorghum.anno.test.modular.wtf;

import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.suppose.model.BaseMetaModel;
import site.sorghum.anno.suppose.proxy.BaseAnnoPreProxy;
import site.sorghum.anno.suppose.proxy.VirtualJoinTableProxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class WtfABCVirtualProxy extends VirtualJoinTableProxy<WtfABCVirtual> {
    @Inject
    BaseAnnoPreProxy baseAnnoPreProxy;

    @Inject
    MetadataManager metadataManager;

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(WtfABCVirtual.class)
        };
    }

    @Override
    public void beforeAdd(WtfABCVirtual data) {
        WtfA wtfA = new WtfA();
        baseAnnoPreProxy.beforeAdd(wtfA);
        wtfA.setAge(data.getAge());
        wtfA.setName(data.getName());

        WtfB wtfB = new WtfB();
        baseAnnoPreProxy.beforeAdd(wtfB);
        wtfB.setAttr(data.getAttr());
        wtfB.setWtfA(wtfA.getId());

        WtfC wtfC = new WtfC();
        baseAnnoPreProxy.beforeAdd(wtfC);
        wtfC.setLocation(data.getLocation());
        wtfC.setWtfB(wtfB.getId());

        getDbServiceWood().insert(wtfA);
        getDbServiceWood().insert(wtfB);
        getDbServiceWood().insert(wtfC);

    }

    @Override
    public void afterAdd(WtfABCVirtual data) {
        super.afterAdd(data);
    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, WtfABCVirtual data) {
        TableParam<BaseMetaModel> wtfAParam = metadataManager.getTableParam(WtfA.class);
        TableParam<BaseMetaModel> wtfBParam = metadataManager.getTableParam(WtfB.class);
        TableParam<BaseMetaModel> wtfCParam = metadataManager.getTableParam(WtfC.class);

        ArrayList<DbCondition> aConditions = CollUtil.newArrayList(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "id", data.getT3id()));
        getDbServiceWood().update(aConditions, new WtfA(data.name, data.age,null));

        ArrayList<DbCondition> bConditions = CollUtil.newArrayList(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "id", data.getT2id()));
        getDbServiceWood().update(bConditions, new WtfB(data.attr,null,null));

        ArrayList<DbCondition> cConditions = CollUtil.newArrayList(new DbCondition(DbCondition.QueryType.EQ, DbCondition.AndOr.AND, "id", data.getId()));
        getDbServiceWood().update(cConditions, new WtfC(data.location,null));

    }

    @SneakyThrows
    @Override
    public void afterFetch(Class<WtfABCVirtual> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<WtfABCVirtual> page) {
        // 系统仅支持查询，其余如新增、修改、删除等操作请自行实现。
        super.afterFetch(tClass, dbConditions, pageParam, page);
    }

    @SneakyThrows
    @Override
    public void beforeDelete(Class<WtfABCVirtual> tClass, List<DbCondition> dbConditions) {
        log.info("before delete:{}", dbConditions);
        // 查询要删除的数据原信息
        WtfABCVirtual wtfABCVirtual = getDbServiceWood().queryOne(tClass, dbConditions);
        log.info("before delete:{}", wtfABCVirtual);
        super.beforeDelete(tClass, dbConditions);
    }
}
