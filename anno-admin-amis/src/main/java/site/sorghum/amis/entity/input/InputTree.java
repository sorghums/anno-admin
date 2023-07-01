package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.Options;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputTree extends FormItem {
    {
        setType("input-tree");
    }
    //options	Array<object>或Array<string>		选项组
    //source	string或 API		动态选项组
    //autoComplete	API		自动提示补全
    //multiple	boolean	false	是否多选
    //delimiter	string	false	拼接符
    //labelField	string	"label"	选项标签字段
    //valueField	string	"value"	选项值字段
    //iconField	string	"icon"	图标值字段
    //joinValues	boolean	true	拼接值
    //extractValue	boolean	false	提取值
    //creatable	boolean	false	新增选项
    //addControls	Array<表单项>		自定义新增表单项
    //addApi	API		配置新增选项接口
    //editable	boolean	false	编辑选项
    //editControls	Array<表单项>		自定义编辑表单项
    //editApi	API		配置编辑选项接口
    //removable	boolean	false	删除选项
    //deleteApi	API		配置删除选项接口
    //searchable	boolean	false	是否可检索	2.8.0前仅tree-select支持
    //hideRoot	boolean	true	如果想要显示个顶级节点，请设置为 false
    //rootLabel	boolean	"顶级"	当 hideRoot 不为 false 时有用，用来设置顶级节点的文字。
    //showIcon	boolean	true	是否显示图标
    //showRadio	boolean	false	是否显示单选按钮，multiple 为 false 是有效。
    //showOutline	boolean	false	是否显示树层级展开线
    //initiallyOpen	boolean	true	设置是否默认展开所有层级。
    //unfoldedLevel	number	1	设置默认展开的级数，只有initiallyOpen不是true时生效。
    //autoCheckChildren	boolean	true	当选中父节点时级联选择子节点。
    //cascade	boolean	false	autoCheckChildren 为 true 时生效；默认行为：子节点禁用，值只包含父节点值；设置为 true 时，子节点可反选，值包含父子节点值。
    //withChildren	boolean	false	cascade 为 false 时生效，选中父节点时，值里面将包含父子节点的值，否则只会保留父节点的值。
    //onlyChildren	boolean	false	autoCheckChildren 为 true 时生效，不受 cascade 影响；onlyChildren 为 true，ui 行为级联选中子节点，子节点可反选，值只包含子节点的值。
    //onlyLeaf	boolean	false	只允许选择叶子节点
    //rootCreatable	boolean	false	是否可以创建顶级节点
    //rootCreateTip	string	"添加一级节点"	创建顶级节点的悬浮提示
    //minLength	number		最少选中的节点数
    //maxLength	number		最多选中的节点数
    //treeContainerClassName	string		tree 最外层容器类名
    //enableNodePath	boolean	false	是否开启节点路径模式
    //pathSeparator	string	/	节点路径的分隔符，enableNodePath为true时生效
    //highlightTxt	string		标签中需要高亮的字符，支持变量
    //itemHeight	number	32	每个选项的高度，用于虚拟渲染
    //virtualThreshold	number	100	在选项数量超过多少时开启虚拟渲染
    //menuTpl	string		选项自定义渲染 HTML 片段	2.8.0
    //enableDefaultIcon	boolean	true	是否为选项添加默认的前缀 Icon，父节点默认为folder，叶节点默认为file	2.8.0
    //heightAuto	boolean	false	默认高度会有个 maxHeight，即超过一定高度就会内部滚动，如果希望自动增长请设置此属性

    /**
     * 选项组
     */
    List<Options.Option> options;

    /**
     * 动态选项组
     */
    Api source;

    /**
     * 自动提示补全
     */
    Api autoComplete;

    /**
     * 是否多选
     */
    Boolean multiple;

    /**
     * 拼接符
     */
    String delimiter;

    /**
     * 选项标签字段
     */
    String labelField;

    /**
     * 选项值字段
     */
    String valueField;

    /**
     * 图标值字段
     */
    String iconField = "icon";

    /**
     * 拼接值
     */
    Boolean joinValues = true;

    /**
     * 提取值
     */
    Boolean extractValue;

    /**
     * 新增选项
     */
    Boolean creatable;

    /**
     * 自定义新增表单项
     */
    List<FormItem> addControls;

    /**
     * 配置新增选项接口
     */
    Api addApi;

    /**
     * 编辑选项
     */
    Boolean editable;

    /**
     * 自定义编辑表单项
     */
    List<FormItem> editControls;

    /**
     * 配置编辑选项接口
     */
    Api editApi;

    /**
     * 删除选项
     */
    Boolean removable;

    /**
     * 配置删除选项接口
     */
    Api deleteApi;

    /**
     * 是否可检索
     */
    Boolean searchable;

    /**
     * 如果想要显示个顶级节点，请设置为 false
     */
    Boolean hideRoot;

    /**
     * 当 hideRoot 不为 false 时有用，用来设置顶级节点的文字。
     */
    String rootLabel;

    /**
     * 是否显示图标
     */
    Boolean showIcon = true;

    /**
     * 是否显示单选按钮，multiple 为 false 是有效。
     */
    Boolean showRadio;

    /**
     * 是否显示树层级展开线
     */
    Boolean showOutline;

    /**
     * 设置是否默认展开所有层级。
     */
    Boolean initiallyOpen = true;

    /**
     * 设置默认展开的级数，只有initiallyOpen不是true时生效。
     */
    Integer unfoldedLevel = 1;

    /**
     * 当选中父节点时级联选择子节点。
     */
    Boolean autoCheckChildren = true;

    /**
     * autoCheckChildren 为 true 时生效；默认行为：子节点禁用，值只包含父节点值；设置为 true 时，子节点可反选，值包含父子节点值。
     */
    Boolean cascade;

    /**
     * cascade 为 false 时生效，选中父节点时，值里面将包含父子节点的值，否则只会保留父节点的值。
     */
    Boolean withChildren;

    /**
     * autoCheckChildren 为 true 时生效，不受 cascade 影响；onlyChildren 为 true，ui 行为级联选中子节点，子节点可反选，值只包含子节点的值。
     */
    Boolean onlyChildren;

    /**
     * 只允许选择叶子节点
     */
    Boolean onlyLeaf;

    /**
     * 是否可以创建顶级节点
     */
    Boolean rootCreatable;

    /**
     * 创建顶级节点的悬浮提示
     */
    String rootCreateTip = "添加一级节点";

    /**
     * 最少选中的节点数
     */
    Integer minLength;

    /**
     * 最多选中的节点数
     */
    Integer maxLength;

    /**
     * tree 最外层容器类名
     */
    String treeContainerClassName;

    /**
     * 是否开启节点路径模式
     */
    Boolean enableNodePath;

    /**
     * 节点路径的分隔符，enableNodePath为true时生效
     */
    String pathSeparator = "/";

    /**
     * 标签中需要高亮的字符，支持变量
     */
    String highlightTxt;

    /**
     * 每个选项的高度，用于虚拟渲染
     */
    Integer itemHeight = 32;

    /**
     * 在选项数量超过多少时开启虚拟渲染
     */
    Integer virtualThreshold = 100;

    /**
     * 选项自定义渲染 HTML 片段
     */
    String menuTpl;

    /**
     * 是否为选项添加默认的前缀 Icon，父节点默认为folder，叶节点默认为file
     */
    Boolean enableDefaultIcon = true;

    /**
     * 默认高度会有个 maxHeight，即超过一定高度就会内部滚动，如果希望自动增长请设置此属性
     */
    Boolean heightAuto;
}
