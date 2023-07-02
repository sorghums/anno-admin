package site.sorghum.amis.entity.layout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Page extends AmisBase {
    {
        setType("page");
    }

    /**
     * 页面标题
     */
    String title;

    /**
     * 页面副标题
     */
    String subTitle;

    /**
     * 标题附近会出现一个提示图标，鼠标放上去会提示该内容。
     */
    String remark;

    /**
     * 往页面的边栏区域加内容
     */
    AmisBase aside;

    /**
     * 页面的边栏区域宽度是否可调整
     */
    Boolean asideResizor;

    /**
     * 页面边栏区域的最小宽度
     */
    Integer asideMinWidth;

    /**
     * 页面边栏区域的最大宽度
     */
    Integer asideMaxWidth;

    /**
     * 用来控制边栏固定与否
     */
    Boolean asideSticky;

    /**
     * 往页面的右上角加内容，需要注意的是，当有 title 时，该区域在右上角，没有时该区域在顶部
     */
    AmisBase toolbar;

    /**
     * 往页面的内容区域加内容
     */
    List<AmisBase> body;

    /**
     * 自定义 CSS 变量，请参考样式
     */
    String cssVars;

    /**
     * Toolbar dom 类名
     */
    String toolbarClassName = "v-middle wrapper text-right bg-light b-b";

    /**
     * Body dom 类名
     */
    String bodyClassName = "wrapper";


    /**
     * Aside dom 类名
     */
    String asideClassName = "w page-aside-region bg-auto";

    /**
     * Header 区域 dom 类名
     */
    String headerClassName = "bg-light b-b wrapper";

    /**
     * Page 用来获取初始数据的 api。返回的数据可以整个 page 级别使用。
     */
    Api initApi;

    /**
     * 是否起始拉取 initApi
     */
    Boolean initFetch = true;

    /**
     * 是否起始拉取 initApi, 通过表达式配置
     */
    String initFetchOn;

    /**
     * 刷新时间(最小 1000)
     */
    Integer interval;

    /**
     * 配置刷新时是否显示加载动画
     */
    Boolean silentPolling = true;

    /**
     * 通过表达式来配置停止刷新的条件
     */
    String stopAutoRefreshWhen;

}
