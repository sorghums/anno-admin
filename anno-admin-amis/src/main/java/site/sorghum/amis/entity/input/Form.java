package site.sorghum.amis.entity.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

/**
 * Amis的Form组件
 *
 * @author sorghum
 * @since 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Form extends AmisBase {
    {
        //初始化数据
        setType("form");
    }
    /**
     * 表单展示方式，可以是：normal、horizontal 或者 inline
     */
    private String mode;

    /**
     * 当 mode 为 horizontal 时有用，用来控制 label 的展示占比
     */
    private FormHorizontal horizontal;

    /**
     * 表单项标签对齐方式，默认右对齐，仅在 mode为horizontal 时生效
     * 可选值：right、left
     */
    private String labelAlign;

    /**
     * 表单项标签自定义宽度
     */
    private Integer labelWidth;

    /**
     * Form 的标题
     */
    private String title;


    /**
     * 默认的提交按钮名称，如果设置成空，则可以把默认按钮去掉。
     */
    private String submitText = "提交";

    /**
     * Form 表单项集合
     * Array<表单项 or SchemaNode >
     */
    List<AmisBase> body;

    /**
     * Form 提交按钮，成员为 Action
     * Array<行为按钮>
     */
    List<Action> actions;

    /**
     * 消息提示覆写，默认消息读取的是 API 返回的消息，但是在此可以覆写它。
     */
    private FormMessage messages;

    /**
     * 是否让 Form 用 panel 包起来
     * 设置为 false 后，actions 将无效
     */
    Boolean wrapWithPanel = true;

    /**
     * 外层 panel 的类名
     */
    String panelClassName;

    /**
     * Form 用来保存数据的 api。
     */
    Api api;

    /**
     * Form 用来获取初始数据的 api。
     */
    Api initApi;

    /**
     * 表单组合校验规则
     * Array<{rule:string;message:string;name?: string[]}>
     */
    List rules;

    /**
     * 刷新时间(最低 3000)
     */
    Long interval;

    /**
     * 配置刷新时是否显示加载动画
     */
    Boolean silentPolling = false;

    /**
     * 通过表达式 来配置停止刷新的条件
     */
    String stopAutoRefreshWhen;

    /**
     * Form 用来获取初始数据的 api
     * 与 initApi 不同的是，会一直轮询请求该接口，直到返回 finished 属性为 true 才 结束。
     */
    Api initAsyncApi;

    /**
     * 设置了 initApi 或者 initAsyncApi 后，默认会开始就发请求，设置为 false 后就不会起始就请求接口
     */
    Boolean initFetch = true;

    /**
     * 用表达式来配置
     */
    String initFetchOn;

    /**
     * 设置了 initAsyncApi 后
     * 默认会从返回数据的 data.finished 来判断是否完成
     * 也可以设置成其他的 xxx，就会从 data.xxx 中获取
     */
    String initFinishedField = "finished";

    /**
     * 设置了 initAsyncApi 以后，默认拉取的时间间隔
     */
    Long initCheckInterval;

    /**
     * 设置此属性后，表单提交发送保存接口后
     * 还会继续轮询请求该接口，直到返回 finished 属性为 true 才 结束。
     */
    Api asyncApi;

    /**
     * 轮询请求的时间间隔，默认为 3 秒。设置 asyncApi 才有效
     */
    Long checkInterval;

    /**
     * 如果决定结束的字段名不是 finished 请设置此属性，比如 is_success
     */
    String finishedField = "finished";

    /**
     * 表单修改即提交
     */
    Boolean submitOnChange = false;

    /**
     * 初始就提交一次
     */
    Boolean submitOnInit = false;

    /**
     * 提交后是否重置表单
     */
    Boolean resetAfterSubmit = false;

    /**
     * 设置主键 id, 当设置后，检测表单是否完成时（asyncApi），只会携带此数据。
     */
    String primaryField;

    /**
     * 默认表单提交自己会通过发送 api 保存数据，
     * 但是也可以设定另外一个 form 的 name 值，
     * 或者另外一个 CRUD 模型的 name 值。
     * 如果 target 目标是一个 Form ，
     * 则目标 Form 会重新触发 initApi，
     * api 可以拿到当前 form 数据。
     * 如果目标是一个 CRUD 模型，则目标模型会重新触发搜索，参数为当前 Form 数据。
     * 当目标是 window 时，会把当前表单的数据附带到页面地址上。
     */
    String target;

    /**
     * 设置此属性后，Form 保存成功后，自动跳转到指定页面。支持相对地址，和绝对地址（相对于组内的）。
     */
    String redirect;

    /**
     * 操作完后刷新目标对象。请填写目标组件设置的 name 值，如果填写为 window 则让当前页面整体刷新。
     */
    String reload;

    /**
     * 是否自动聚焦。
     */
    Boolean autoFocus = false;

    /**
     * 指定是否可以自动获取上层的数据并映射到表单项上
     */
    Boolean canAccessSuperData = true;

    /**
     * 指定一个唯一的 key，来配置当前表单是否开启本地缓存
     */
    String persistData;

    /**
     * 指指定只有哪些 key 缓存
     */
    List<String> persistDataKeys;

    /**
     * 指定表单提交成功后是否清除本地缓存
     */
    Boolean clearPersistDataAfterSubmit = true;

    /**
     * 禁用回车提交表单
     */
    Boolean preventEnterSubmit = false;

    /**
     * trim 当前表单项的每一个值
     */
    Boolean trimValues = false;

    /**
     * form 还没保存，即将离开页面前是否弹框确认。
     */
    Boolean promptPageLeave = false;

    /**
     * 表单项显示为几列
     */
    Integer columnCount;

    /**
     * 默认表单是采用数据链的形式创建个自己的数据域，表单提交的时候只会发送自己这个数据域的数据，
     * 如果希望共用上层数据域可以设置这个属性为 false，
     * 这样上层数据域的数据不需要在表单中用隐藏域或者显式映射才能发送了。
     */
    Boolean inheritData = true;

    /**
     * 整个表单静态方式展示(仅作展示态)
     */
    @JSONField(name = "static")
    Boolean bStatic = false;

    /**
     * 表单静态展示时使用的类名
     */
    String staticClassName;

    /**
     * 提交的时候是否关闭弹窗。
     * 当 form 里面有且只有一个弹窗的时候，本身提交会触发弹窗关闭，此属性可以关闭此行为
     */
    Boolean closeDialogOnSubmit;




    @Data
    static class FormHorizontal {
        /**
         * 左侧宽度
         */
        Integer left;

        /**
         * 右侧宽度
         */
        Integer right;

        /**
         * 是否自动调整
         */
        Boolean justify = false;
    }


    @Data
    static class FormMessage {
        /**
         * 获取成功时提示
         */
        String fetchSuccess;

        /**
         * 获取失败时提示
         */
        String fetchFailed;

        /**
         * 提交成功时提示
         */
        String saveSuccess;

        /**
         * 提交失败时提示
         */
        String saveFailed;
    }

}
