package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.solon.i18n.I18nUtil;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

/**
 * 输入树
 *
 * @author Sorghum
 * @since 2023/07/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputTree extends FormItem {
    {
        setType("input-tree");
    }

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
    String rootCreateTip = I18nUtil.getMessage("amis.input-tree.root-create-tip");

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
