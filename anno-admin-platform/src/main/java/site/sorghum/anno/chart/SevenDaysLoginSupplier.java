package site.sorghum.anno.chart;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.chart.supplier.base.IntegerSupplier;

import java.util.Map;

/**
 * 七天登录供应商
 *
 * @author Sorghum
 * @since 2024/02/23
 */
@Named
@Slf4j
public class SevenDaysLoginSupplier implements IntegerSupplier {

    @Db
    DbContext dbContext;

    @Override
    public Integer get(Map<String,Object> param) {
        try {
            return (int) dbContext.table("an_login_log").where("latest_time >= date_sub(curdate(), interval 7 day)").selectCount();
        } catch (Exception e) {
            log.error("图表sevenDaysLogin数据查询异常！" + e.getMessage());
            return null;
        }
    }
}
