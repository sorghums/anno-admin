package site.sorghum.anno.utils.supplier;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.utils.supplier.base.IntegerSupplier;

@Named
@Slf4j
public class TodayLoginSupplier extends IntegerSupplier {

    @Db
    DbContext dbContext;

    @Override
    public Integer get() {
        try {
            return (int) dbContext.table("an_login_log").where("latest_time >= curdate()").selectCount();
        } catch (Exception e) {
            log.error("图表todayLogin数据查询异常！" + e.getMessage());
            return null;
        }
    }
}
