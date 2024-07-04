package site.sorghum.anno.anno.annotation.field.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.tree.TreeDataSupplier;

import java.lang.annotation.Annotation;

/**
 * Anno 树形选择类型实现
 *
 * @author Sorghum
 * @since 2024/07/04
 */
public class AnnoTreeTypeImpl implements AnnoTreeType {
    /**
     * SQL语句, 优先级高于value
     * 必须返回三列，列名分别为 id, label, pid
     */
    private String sql = "";

    /**
     * 树形选择数据
     */
    private TreeData[] value = {};

    /**
     * 自定义数据提供者
     */
    private Class<? extends TreeDataSupplier> supplier = TreeDataSupplier.class;

    /**
     * 树形annoMain注释的类
     */
    private TreeAnnoClassImpl treeAnno = new TreeAnnoClassImpl();

    /**
     * 是否多选
     */
    private boolean isMultiple = false;

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public TreeData[] value() {
        return value;
    }

    @Override
    public Class<? extends TreeDataSupplier> supplier() {
        return supplier;
    }

    @Override
    public TreeAnnoClass treeAnno() {
        return treeAnno;
    }

    @Override
    public boolean isMultiple() {
        return isMultiple;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoTreeType.class;
    }

    /**
     * 树形数据的内部类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TreeDataImpl implements TreeData {
        /**
         * 主键
         */
        private String id;
        /**
         * 显示的标签
         */
        private String label;
        /**
         * 父主键
         */
        private String pid;

        @Override
        public String id() {
            return this.id;
        }

        @Override
        public String label() {
            return this.label;
        }

        @Override
        public String pid() {
            return this.pid;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return TreeData.class;
        }
    }

    /**
     * 树形annoMain注释的类的内部类
     */
    @Data
    public class TreeAnnoClassImpl implements TreeAnnoClass {
        Class<?> annoClass = Object.class;
        String idKey = "id";
        String labelKey = "name";
        String pidKey = "pid";

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
        public String pidKey() {
            return this.pidKey;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return TreeAnnoClass.class;
        }
    }
}