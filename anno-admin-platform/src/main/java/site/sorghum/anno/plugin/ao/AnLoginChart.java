package site.sorghum.anno.plugin.ao;

import site.sorghum.anno.anno.annotation.clazz.AnnoChart;
import site.sorghum.anno.anno.annotation.field.AnnoChartField;
import site.sorghum.anno.anno.enums.AnnoChartType;
import site.sorghum.anno.utils.supplier.*;

/*@AnnoMain(
    name = "登录日志",
    virtualTable = true,
    annoPermission = @AnnoPermission(
        enable = true,
        baseCode = "an_login_chart",
        baseCodeTranslate = "登录日志"
    ),
    canRemove = false
)*/
@AnnoChart(
    name = "登录图表",
    permissionCode = "an_login_chart",
    layout = {4,1,3}
)
public class AnLoginChart {

    @AnnoChartField(
        name = "今日访客",
        type = AnnoChartType.NUMBER,
        runSupplier = TodayLoginSupplier.class,
        order = 1,
        permissionCode = "todayLogin"
    )
    private Object todayLogin;

    @AnnoChartField(
        name = "近7日访客",
        type = AnnoChartType.NUMBER,
        runSupplier = SevenDaysLoginSupplier.class,
        order = 2,
        permissionCode = "sevenDaysLogin"
    )
    private Object sevenDaysLogin;

    @AnnoChartField(
        name = "流量趋势",
        type = AnnoChartType.LINE,
        runSupplier = TrafficTrendSupplier.class,
        order = 3,
        permissionCode = "trafficTrend"
    )
    private Object trafficTrend;

    @AnnoChartField(
        name = "访问设备",
        type = AnnoChartType.PIE,
        runSupplier = AccessDeviceSupplier.class,
        order = 4,
        permissionCode = "accessDevice"
    )
    private Object accessDevice;

    @AnnoChartField(
        name = "访问系统",
        type = AnnoChartType.PIE,
        runSupplier = AccessOsSupplier.class,
        order = 5,
        permissionCode = "accessOs"
    )
    private Object accessOs;

    @AnnoChartField(
        name = "访问浏览器",
        type = AnnoChartType.PIE,
        runSupplier = AccessBrowserSupplier.class,
        order = 6,
        permissionCode = "accessBrowser"
    )
    private Object accessBrowser;
}
