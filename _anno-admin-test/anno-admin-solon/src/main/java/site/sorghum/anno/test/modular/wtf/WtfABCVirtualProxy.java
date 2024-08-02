package site.sorghum.anno.test.modular.wtf;

import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.BaseMetaModel;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbTableContext;
import site.sorghum.anno.db.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.suppose.proxy.BaseAnnoPreProxy;

import java.util.List;

@Component
@Slf4j
public class WtfABCVirtualProxy implements AnnoBaseProxy<WtfABCVirtual> {
    @Inject
    BaseAnnoPreProxy baseAnnoPreProxy;

    @Inject
    DbTableContext dbTableContext;

    @Inject
    DbService dbService;

    @Inject
    MetadataManager metadataManager;

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

        dbService.insert(wtfA);
        dbService.insert(wtfB);
        dbService.insert(wtfC);

    }


    @Override
    public void beforeUpdate(WtfABCVirtual data, DbCriteria criteria) {
        TableParam<BaseMetaModel> wtfAParam = dbTableContext.getTableParam(WtfA.class);
        TableParam<BaseMetaModel> wtfBParam = dbTableContext.getTableParam(WtfB.class);
        TableParam<BaseMetaModel> wtfCParam = dbTableContext.getTableParam(WtfC.class);


        dbService.update(new WtfA(data.name, data.age, null), DbCriteria.fromClass(WtfA.class).eq("id", data.getT3id()));

        dbService.update(new WtfB(data.attr, null, null), DbCriteria.fromClass(WtfB.class).eq("id", data.getT2id()));

        dbService.update(new WtfC(data.location, null), DbCriteria.fromClass(WtfC.class).eq("id", data.getId()));

    }

    @SneakyThrows
    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<WtfABCVirtual> page) {
        TableParam<WtfABCVirtual> tableParam = dbTableContext.getOriginalTableParam(criteria.getEntityName());
        tableParam.setTableName("wtf_a as t1");
        tableParam.setJoinTables(
            List.of(
                TableParam.JoinTable.builder()
                    .joinType(1)
                    .tableName("wtf_b")
                    .alias("t2")
                    .joinCondition("t1.id = t2.wtf_a")
                    .build(),
                TableParam.JoinTable.builder()
                    .joinType(1)
                    .tableName("wtf_c")
                    .alias("t3")
                    .joinCondition("t2.id = t3.wtf_b")
                    .build())
        );
        AnnoPage<WtfABCVirtual> paged = dbService.page(criteria);
        // 系统仅支持查询，其余如新增、修改、删除等操作请自行实现。
        page.setTotal(paged.getTotal());
        page.setList(paged.getList());
        page.setCurrentPage(paged.getCurrentPage());
    }

    @SneakyThrows
    @Override
    public void beforeDelete(DbCriteria criteria) {
        log.info("before delete:{}", criteria);
        // 查询要删除的数据原信息
        WtfABCVirtual wtfABCVirtual = dbService.queryOne(criteria);
        log.info("before delete:{}", wtfABCVirtual);
    }
}
