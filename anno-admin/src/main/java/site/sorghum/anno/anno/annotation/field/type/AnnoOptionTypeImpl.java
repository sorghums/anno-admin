package site.sorghum.anno.anno.annotation.field.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.lang.annotation.Annotation;

/**
 * Anno 选择类型
 *
 * @author Sorghum
 * @since 2024/07/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoOptionTypeImpl implements AnnoOptionType {
    /**
     * SQL缓存主键
     */
    private String sqlKey;

    /**
     * SQL语句, 优先级高于value
     * 必须返回两列，列名分别为 label 和 id
     * 比如 select id, label from table where del_flag = 0 order by id desc
     */
    private String sql = "";

    /**
     * 选择数据
     */
    private OptionDataImpl[] value = {};

    /**
     * 自定义数据提供者
     */
    private Class<? extends OptionDataSupplier> supplier = OptionDataSupplier.class;

    /**
     * 返回一个枚举类型的Class对象，该枚举类型用于表示选项。
     */
    private Class<? extends Enum> optionEnum = Enum.class;

    /**
     * annoMain注释的类，比如 SysOrg.class
     * 最后会执行类似的：select value, label from sys_org where del_flag = 0 order by id desc
     * 并且会自动走SysOrg的代理操作
     */
    private OptionAnnoClassImpl optionAnno = new OptionAnnoClassImpl();

    /**
     * 是否多选，多选的值格式为逗号拼接 value 值
     */
    private boolean isMultiple = false;

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public OptionData[] value() {
        return value;
    }

    @Override
    public Class<? extends OptionDataSupplier> supplier() {
        return supplier;
    }

    @Override
    public Class<? extends Enum> optionEnum() {
        return optionEnum;
    }

    @Override
    public OptionAnnoClass optionAnno() {
        return optionAnno;
    }

    @Override
    public boolean isMultiple() {
        return isMultiple;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoOptionType.class;
    }

    /**
     * 选择数据的内部类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class OptionDataImpl implements OptionData {
        /**
         * 显示的标签
         */
        private String label;
        /**
         * 返回的值
         */
        private String value;

        @Override
        public String label() {
            return this.label;
        }

        @Override
        public String value() {
            return this.value;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return OptionData.class;
        }
    }

    /**
     * annoMain注释的类的内部类
     */
    @Data
    public class OptionAnnoClassImpl implements OptionAnnoClass {

        /**
         * annoMain注释的类，比如 SysOrg.class
         * 最后会执行类似的：select value, label from sys_org where del_flag = 0 order by id desc
         * 并且会自动走SysOrg的代理操作
         */
        Class<?> annoClass = Object.class;

        /**
         * 返回主键的字段名
         */
        String idKey = "id";

        /**
         * 返回主键的字段名
         */
        String labelKey = "name";

        @Override
        public Class<?> annoClass() {
            return this.annoClass;
        }

        @Override
        public String idKey() {
            return this.idKey;
        }

        @Override
        public String labelKey() {
            return this.labelKey;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return OptionAnnoClass.class;
        }
    }
}