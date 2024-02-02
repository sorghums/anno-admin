package site.sorghum.anno.utils.supplier;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.utils.supplier.base.MapListSupplier;

import java.util.List;
import java.util.Map;

@Named
@Slf4j
public class AccessBrowserSupplier extends MapListSupplier {

    @Db
    DbContext dbContext;

    @Override
    public List<Map<String, Object>> get() {
        try {
            return dbContext.sql("select browser as item,count(*) as itemCount from an_login_log group by browser").getMapList();
        } catch (Exception e) {
            log.error("图表accessBrowser数据查询异常！" + e.getMessage());
            return null;
        }
    }
}
