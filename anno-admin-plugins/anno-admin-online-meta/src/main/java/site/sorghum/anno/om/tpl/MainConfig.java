package site.sorghum.anno.om.tpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.util.List;

/**
 * 主配置类，用于存储和管理FTL模板中定义的类和字段配置信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainConfig {

    // 类的名称
    private String name;
    // 数据库表的名称
    private String tableName;
    // 注解顺序配置列表
    private List<AnnoOrder> annoOrder;
    // Java类的名称
    private String className;
    // 字段配置列表
    private List<FieldConfig> fields;
    // 是否可以删除
    private boolean canRemove;
    // 是否自动维护数据库表
    private boolean autoMaintainTable;

    /**
     * 注解顺序配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnoOrder {
        // 注解顺序值
        private String orderValue;
        // 注解顺序类型
        private String orderType;
    }

    /**
     * 字段配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldConfig {
        // 字段标题
        private String title;
        // 数据库表中的字段名称
        private String tableFieldName = "";
        // 字段大小
        private int fieldSize = 255;
        // 是否显示
        private boolean show = true;
        // 是否为主键
        private boolean pkField = false;
        // 编辑配置
        private EditConfig edit;
        // 搜索配置
        private SearchConfig search;
        // 数据类型
        private AnnoDataType dataType;
        // 选项类型配置
        private OptionType optionType;
        // 图像类型配置
        private ImageType imageType;
        // 代码类型配置
        private CodeType codeType;
        // 树类型配置
        private TreeType treeType;
        // 文件类型配置
        private FileType fileType;
        // 字段类型
        private String type;
        // 字段名称
        private String name;
    }

    /**
     * 编辑配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditConfig {
        // 是否允许编辑
        private boolean editEnable = true;
        // 是否允许添加
        private boolean addEnable = true;
    }

    /**
     * 搜索配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchConfig {
        // 是否非空
        private boolean notNull = false;
        // 默认值
        private String defaultValue = "";
    }

    /**
     * 选项类型配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionType {
        // 选项类
        private String optionAnnoClass;
        // 选项数据列表
        private List<OptionData> value;

        /**
         * 选项数据类。
         */
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OptionData {
            // 选项的值
            private String value;
            // 选项的标签
            private String label;
        }
    }

    /**
     * 图像类型配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageType {
        // 是否可以放大
        private boolean enlargeAble = true;
        // 图像宽度
        private int width = 50;
        // 图像高度
        private int height = 50;
    }

    /**
     * 代码类型配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CodeType {
        // 代码模式
        private String mode = "text";
    }

    /**
     * 树类型配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TreeType {
        // 树类
        private String treeAnnoClass;
        // 树数据列表
        private List<TreeData> value;

        /**
         * 树数据类。
         */
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TreeData {
            // 节点ID
            private String id;
            // 节点标签
            private String label;
            // 父节点ID
            private String pid;
        }
    }

    /**
     * 文件类型配置类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileType {
        // 文件类型
        private String fileType = "*";
        // 文件最大数量
        private int fileMaxCount = 1;
        // 文件最大大小
        private long fileMaxSize = 5;
    }
}