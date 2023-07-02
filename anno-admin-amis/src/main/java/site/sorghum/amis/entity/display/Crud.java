package site.sorghum.amis.entity.display;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Crud extends Table {
    {
        setType("crud");
    }
    /**
     * 展示模式
     * "table" 、 "cards" 或者 "list"
     */
    private String mode = "table";

    /**
     * 可设置成空，当设置成空时，没有标题栏
     */
    private String title = "";

    /**
     * 表格外层 Dom 的类名
     */
    private String className;

    /**
     * CRUD 用来获取列表数据的 api。
     */
    private Api api;

    /**
     * 是否一次性加载所有数据（前端分页）
     */
    private Boolean loadDataOnce;

    /**
     * 在开启 loadDataOnce 时，filter 时是否去重新请求 api
     */
    private Boolean loadDataOnceFetchOnFilter;

    /**
     * 数据映射接口返回某字段的值，不设置会默认使用接口返回的${items}或者${rows}，也可以设置成上层数据源的内容
     */
    private Api source;

    /**
     * 设置过滤器，当该表单提交后，会把数据带给当前 mode 刷新列表。
     */
    private Form filter;

    /**
     * 是否可显隐过滤器
     */
    private Boolean filterTogglable = false;

    /**
     * 设置过滤器默认是否可见。
     */
    private Boolean filterDefaultVisible = true;

    /**
     * 是否初始化的时候拉取数据, 只针对有 filter 的情况, 没有 filter 初始都会拉取数据
     */
    private Boolean initFetch = true;

    /**
     * 刷新时间(最低 1000)
     */
    private Integer interval = 3000;

    /**
     * 配置刷新时是否隐藏加载动画
     */
    private Boolean silentPolling = false;

    /**
     * 通过表达式来配置停止刷新的条件
     */
    private String stopAutoRefreshWhen;

    /**
     * 当有弹框时关闭自动刷新，关闭弹框又恢复
     */
    private Boolean stopAutoRefreshWhenModalIsOpen = false;

    /**
     * 是否将过滤条件的参数同步到地址栏
     */
    private Boolean syncLocation = true;

    /**
     * 是否可通过拖拽排序
     */
    private Boolean draggable = false;

    /**
     * 是否可以调整列宽度
     */
    private Boolean resizable = true;


    /**
     * 用表达式来配置是否可拖拽排序
     */
    private String itemDraggableOn;

    /**
     * 保存排序的 api。
     */
    private Api saveOrderApi;

    /**
     * 快速编辑后用来批量保存的 API。
     */
    private Api quickSaveApi;

    /**
     * 快速编辑配置成及时保存时使用的 API。
     */
    private Api quickSaveItemApi;

    /**
     * 批量操作列表，配置后，表格可进行选中操作。
     */
    private List<Action> bulkActions;

    /**
     * 覆盖消息提示，如果不指定，将采用 api 返回的 message
     */
    private CrudMessages messages;

    /**
     * 设置 ID 字段名。
     */
    private String primaryField = "id";

    /**
     * 设置一页显示多少条数据。
     */
    private Integer perPage = 10;

    /**
     * 设置一页显示多少条数据下拉框可选条数。
     */
    private List<Integer> perPageAvailable = CollUtil.newArrayList(5, 10, 20, 50, 100);

    /**
     * 设置用来确定位置的字段名，设置后新的顺序将被赋值到该字段中。
     */
    private String orderField;

    /**
     * 隐藏顶部快速保存提示
     */
    private Boolean hideQuickSaveBtn = false;

    /**
     * 当切分页的时候，是否自动跳顶部。
     */
    private Boolean autoJumpToTopOnPagerChange = false;

    /**
     * 将返回数据同步到过滤器上。
     */
    private Boolean syncResponse2Query = true;

    /**
     * 保留条目选择，默认分页、搜素后，用户选择条目会被清空，开启此选项后会保留用户选择，可以实现跨页面批量操作。
     */
    private Boolean keepItemSelectionOnPageChange = false;

    /**
     * 单条描述模板，keepItemSelectionOnPageChange设置为true后会把所有已选择条目列出来，此选项可以用来定制条目展示文案。
     */
    private String labelTpl;

    /**
     * 是否显示表格边框
     */
    private Boolean bordered = true;

    /**
     * 顶部工具栏配置
     */
    private List<String> headerToolbar = CollUtil.newArrayList("bulkActions", "pagination");

    /**
     * 底部工具栏配置
     */
    private List<String> footerToolbar = CollUtil.newArrayList("statistics", "pagination");

    /**
     * 是否总是显示分页
     */
    private Boolean alwaysShowPagination = false;

    /**
     * 是否固定表头(table 下)
     */
    private Boolean fixedHeader = false;

    /**
     * 是否开启查询区域，开启后会根据列元素的 searchable 属性值，自动生成查询条件表单
     */
    private Object autoGenerateFilter;

    /**
     * 单条数据 ajax 操作后是否重置页码为第一页
     */
    private Boolean resetPageAfterAjaxItemAction = false;

    /**
     * 内容区域自适应高度
     *  boolean | {height: number}
     */
    private Object autoFillHeight = true;




    @Data
    public static class CrudMessages {
        /**
         * 保存失败提示
         */
        private String fetchFailed;
        /**
         * 保存失败提示
         */
        private String saveOrderFailed;
        /**
         * 保存成功提示
         */
        private String saveOrderSuccess;
        /**
         * 保存失败提示
         */
        private String quickSaveFailed;
        /**
         * 保存成功提示
         */
        private String quickSaveSuccess;

    }


}
