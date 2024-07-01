package site.sorghum.anno.plugin.chart;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.anno.chart.supplier.IntegerSupplier;
import site.sorghum.anno._common.entity.CommonParam;

/**
 * 今天登录供应商
 *
 * @author Sorghum Qjw
 * @since 2024/02/23
 */
@Named
@Slf4j
public class TodayLoginSupplier implements IntegerSupplier {

    @Db
    DbContext dbContext;

    @Override
    public Integer get(CommonParam param) {
        try {
            return (int) dbContext.table("an_login_log").where("latest_time >= curdate()").selectCount();
        } catch (Exception e) {
            log.error("图表todayLogin数据查询异常！" + e.getMessage());
            return null;
        }
    }
}
