package site.sorghum.anno.plugin.ao;

import site.sorghum.anno.anno.annotation.clazz.AnnoChart;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoChartField;
import site.sorghum.anno.anno.enums.AnnoChartType;
import site.sorghum.anno.chart.*;

/**
 * 登录图表
 *
 * @author qjw
 * @since 2024/02/22
 */
@AnnoMain(
    name = "登陆图表",
    annoPermission = @AnnoPermission(
        enable = true,
        baseCode = "an_login_chart",
        baseCodeTranslate = "登陆图表"
    ),
    annoChart = @AnnoChart(
        layout = {2, 1, 3},
        chartFields = {
            @AnnoChartField(
                name = "今日登陆",
                type = AnnoChartType.NUMBER,
                runSupplier = TodayLoginSupplier.class,
                order = 1,
                permissionCode = "todayLogin",
                action = "日",
                actionColor = "red"
            ),
            @AnnoChartField(
                name = "近7日登陆",
                type = AnnoChartType.NUMBER,
                runSupplier = SevenDaysLoginSupplier.class,
                order = 2,
                permissionCode = "sevenDaysLogin",
                action = "日"),
            @AnnoChartField(
                name = "登陆趋势",
                type = AnnoChartType.BAR,
                runSupplier = TrafficTrendSupplier.class,
                order = 3,
                permissionCode = "trafficTrend",
                action = "日"
            ),
            @AnnoChartField(
                name = "访问设备",
                type = AnnoChartType.PIE,
                runSupplier = AccessDeviceSupplier.class,
                order = 4,
                permissionCode = "accessDevice",
                action = "日"
            ),
            @AnnoChartField(
                name = "访问系统",
                type = AnnoChartType.PIE,
                runSupplier = AccessOsSupplier.class,
                order = 5,
                permissionCode = "accessOs",
                action = "日"
            ),
            @AnnoChartField(
                name = "访问浏览器",
                type = AnnoChartType.PIE,
                runSupplier = AccessBrowserSupplier.class,
                order = 6,
                permissionCode = "accessBrowser",
                action = "日"
            )

        }
    ),
    virtualTable = true
)
public class AnLoginChart {

}
