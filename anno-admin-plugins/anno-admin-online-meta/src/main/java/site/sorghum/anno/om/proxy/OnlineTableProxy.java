package site.sorghum.anno.om.proxy;

import jakarta.inject.Named;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DataItem;
import org.noear.wood.DataList;
import org.noear.wood.DbContext;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.om.ao.OnlineTable;
import site.sorghum.anno.om.supplier.DsNameSupplier;

@Named
@Slf4j
public class OnlineTableProxy implements AnnoBaseProxy<OnlineTable> {
    @SneakyThrows
    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<OnlineTable> page) {
        DbPage criteriaPage = criteria.getPage();
        String dsName = getValueStringFromJavaCriteria(criteria.condition(), OnlineTable.class, "dsName");
        DbContext dbContext = DsNameSupplier.dbContexts.get(dsName);
        if (dbContext == null){
            throw new BizException("dsName: " + dsName + "对应的数据库连接不存在");
        }
        log.info("dbContext:{}",dbContext.getMetaData().getSchema());
        // 获取当前页码和每页数量
        long pageNumber = criteriaPage.getPage();
        long pageSize = criteriaPage.getPageSize();
        // 计算偏移量
        long offset = (pageNumber - 1) * pageSize;
        // 分页查询表名
        DataList dataList = dbContext.sql("""
            select table_name
            from information_schema.tables
            limit ? offset ?
            """, pageSize, offset).getDataList();
        long count = dbContext.sql("""
            select count(*)
            from information_schema.tables
            """).getCount();
        page.setTotal(count);
        for (DataItem dataItem : dataList) {
            OnlineTable onlineTable = new OnlineTable();
            String tableName = dataItem.getString("table_name");
            onlineTable.setId(tableName);
            onlineTable.setDsName(dsName);
            onlineTable.setTableName(tableName);
            page.getList().add(onlineTable);
        }
        log.info("afterFetch: {}", page);
        AnnoBaseProxy.super.afterFetch(criteria, page);
    }
}
