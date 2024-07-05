package site.sorghum.anno.anno.annotation.field;

import lombok.Data;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.anno.tpl.BaseTplRender;

import java.lang.annotation.Annotation;

/**
 * Anno Button 注解
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
public class AnnoButtonImpl implements AnnoButton {
    /**
     * 按钮名称
     */
    private String name;

    /**
     * 按钮权限编码
     */
    private String permissionCode = "";

    /**
     * 图标
     */
    private String icon = "ant-design:appstore-filled";

    /**
     * 按钮大小 	'xs' | 'sm' | 'md' | 'lg'
     */
    private String size = "sm";

    /**
     * 表单提供类
     */
    private Class<? extends BaseForm> baseForm = DefaultBaseForm.class;

    //----------------------- 以下为按钮事件 -----------------------

    /**
     * 按下按钮后的js命令
     */
    private String jsCmd = "";

    /**
     * 跳转的url
     */
    private String jumpUrl = "";

    /**
     * 一对多关联按钮
     */
    private O2MJoinButtonImpl o2mJoinButton = new O2MJoinButtonImpl(){{
        setEnable(false);
    }};

    /**
     * 多对多关联设置【新】
     */
    private M2MRelationImpl m2mRelation = new M2MRelationImpl(){{
        setEnable(false);
    }};

    /**
     * 多对多关联按钮
     */
    private M2MJoinButtonImpl m2mJoinButton = new M2MJoinButtonImpl(){{
        setEnable(false);
    }};

    /**
     * java命令行
     */
    private JavaCmdImpl javaCmd = new JavaCmdImpl(){{
        setEnable(false);
    }};

    /**
     * 下钻按钮【未启用】
     */
    private DrillDownButtonImpl drillDownButton = new DrillDownButtonImpl(){{
        setEnable(false);
    }};

    /**
     * 模板视图按钮
     */
    private AnnoTplImpl annoTpl = new AnnoTplImpl(){{
        setEnable(false);
    }};

    @Override
    public String name() {
        return name;
    }

    @Override
    public String icon() {
        return icon;
    }

    @Override
    public String size() {
        return size;
    }

    @Override
    public Class<? extends BaseForm> baseForm() {
        return baseForm;
    }

    @Override
    public String jsCmd() {
        return jsCmd;
    }

    @Override
    public String jumpUrl() {
        return jumpUrl;
    }

    @Override
    public O2MJoinButton o2mJoinButton() {
        return o2mJoinButton;
    }

    @Override
    public M2MRelation m2mRelation() {
        return m2mRelation;
    }

    @Override
    public M2MJoinButton m2mJoinButton() {
        return m2mJoinButton;
    }

    @Override
    public JavaCmd javaCmd() {
        return javaCmd;
    }

    @Override
    public DrillDownButton drillDownButton() {
        return drillDownButton;
    }

    @Override
    public AnnoTpl annoTpl() {
        return annoTpl;
    }

    @Override
    public String permissionCode() {
        return this.permissionCode;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoButton.class;
    }

    /**
     * 一对多关联按钮的内部类
     */
    @Data
    public static class O2MJoinButtonImpl implements O2MJoinButton {
        /**
         * 连表查询
         */
        private Class<?> targetClass = Object.class;

        /**
         * 以哪个字段为条件【this】
         */
        private String thisJavaField = "";

        /**
         * 以哪个字段为条件【target】
         */
        private String targetJavaField = "";

        /**
         * 启用
         */
        private boolean enable = true;

        @Override
        public Class<?> targetClass() {
            return targetClass;
        }

        @Override
        public String thisJavaField() {
            return thisJavaField;
        }

        @Override
        public String targetJavaField() {
            return targetJavaField;
        }

        @Override
        public boolean enable() {
            return enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return O2MJoinButton.class;
        }
    }

    /**
     * 多对多关联设置的内部类
     */
    @Data
    public static class M2MRelationImpl implements M2MRelation {
        /**
         * 对象目标类
         */
        private Class<?> joinTargetClass = Object.class;

        /**
         * 以哪个字段为条件【目标表】
         */
        private String joinTargetField = "id";

        /**
         * SQL语句：? 为 joinThisClazzField的值
         * demo1: select user_id from an_user_role where role_id = ?
         * demo2: select role_id from an_user_role where user_id = ?
         */
        private String joinSql = "";

        /**
         * 以哪个字段为条件【本】
         */
        private String joinThisField = "id";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         */
        private String windowSize = "xl";

        /**
         * 弹出窗口高度
         */
        private String windowHeight = "700px";

        /**
         * 启用
         */
        private boolean enable = true;

        @Override
        public Class<?> joinTargetClass() {
            return joinTargetClass;
        }

        @Override
        public String joinTargetField() {
            return joinTargetField;
        }

        @Override
        public String joinSql() {
            return joinSql;
        }

        @Override
        public String joinThisField() {
            return joinThisField;
        }

        @Override
        public String windowSize() {
            return windowSize;
        }

        @Override
        public String windowHeight() {
            return windowHeight;
        }

        @Override
        public boolean enable() {
            return enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return M2MRelation.class;
        }
    }

    /**
     * 多对多关联按钮的内部类
     */
    @Data
    public static class M2MJoinButtonImpl implements M2MJoinButton {
        /**
         * 目标表
         */
        private Class<?> joinTargetClazz = Object.class;

        /**
         * 以哪个字段为条件【this】
         */
        private String joinThisClazzField = "id";

        /**
         * 以哪个字段为条件【target】
         */
        private String joinTargetClazzField = "id";

        /**
         * 中间表的类
         */
        private Class<?> mediumTableClazz = Object.class;

        /**
         * 中间表的字段【目标表】
         */
        private String mediumTargetField = "";

        /**
         * 中间表的字段【本表】
         */
        private String mediumThisField = "";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         */
        private String windowSize = "xl";

        /**
         * 启用
         */
        private boolean enable = true;

        @Override
        public Class<?> joinTargetClazz() {
            return joinTargetClazz;
        }

        @Override
        public String joinThisClazzField() {
            return joinThisClazzField;
        }

        @Override
        public String joinTargetClazzField() {
            return joinTargetClazzField;
        }

        @Override
        public Class<?> mediumTableClazz() {
            return mediumTableClazz;
        }

        @Override
        public String mediumTargetField() {
            return mediumTargetField;
        }

        @Override
        public String mediumThisField() {
            return mediumThisField;
        }

        @Override
        public String windowSize() {
            return windowSize;
        }

        @Override
        public boolean enable() {
            return enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return M2MJoinButton.class;
        }
    }

    /**
     * java命令行的内部类
     */
    @Data
    public static class JavaCmdImpl implements JavaCmd {
        /**
         * 运行供应商
         */
        private Class<? extends JavaCmdSupplier> runSupplier = JavaCmdSupplier.class;

        /**
         * 启用
         */
        private boolean enable = true;

        @Override
        public Class<? extends JavaCmdSupplier> runSupplier() {
            return runSupplier;
        }

        @Override
        public boolean enable() {
            return enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return JavaCmd.class;
        }
    }

    /**
     * 下钻按钮的内部类
     */
    @Data
    public static class DrillDownButtonImpl implements DrillDownButton {
        /**
         * 用于数据展示的查询
         * select * from sys_user t1 left join sys_org t2 where user_id = ?
         */
        private String fetchSql = "";

        /**
         * 以哪个字段为条件【本表】
         */
        private String thisField = "";

        /**
         * 以哪个字段为条件【目标表】
         */
        private String targetField = "";

        /**
         * 中间表
         */
        private Class<?> mediumTableClass = Object.class;

        /**
         * 中间表 的字段【目标表】
         */
        private String mediumOtherField = "";

        /**
         * 中间表 的字段【本表】
         */
        private String mediumThisField = "";

        /**
         * 是否是树形结构
         */
        private boolean isTree = false;

        /**
         * 树形结构 的id字段
         */
        private String treeIdField = "id";

        /**
         * 树形结构 的父id字段
         */
        private String treeParentIdField = "pid";

        /**
         * 树形结构 的label字段
         */
        private String treeLabelField = "name";

        /**
         * 弹出窗口大小
         * xs、sm、md、lg、xl、full
         */
        private String windowSize = "xl";

        /**
         * 弹出窗口高度
         */
        private String windowHeight = "700px";

        /**
         * 是否启用
         */
        private boolean enable = true;

        @Override
        public String fetchSql() {
            return fetchSql;
        }

        @Override
        public String thisField() {
            return thisField;
        }

        @Override
        public String targetField() {
            return targetField;
        }

        @Override
        public Class<?> mediumTableClass() {
            return mediumTableClass;
        }

        @Override
        public String mediumOtherField() {
            return mediumOtherField;
        }

        @Override
        public String mediumThisField() {
            return mediumThisField;
        }

        @Override
        public boolean isTree() {
            return isTree;
        }

        @Override
        public String treeIdField() {
            return treeIdField;
        }

        @Override
        public String treeParentIdField() {
            return treeParentIdField;
        }

        @Override
        public String treeLabelField() {
            return treeLabelField;
        }

        @Override
        public String windowSize() {
            return windowSize;
        }

        @Override
        public String windowHeight() {
            return windowHeight;
        }

        @Override
        public boolean enable() {
            return enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return DrillDownButton.class;
        }
    }

    /**
     * 模板视图按钮的内部类
     */
    @Data
    public static class AnnoTplImpl implements AnnoTpl {
        /**
         * 启用
         */
        private boolean enable = false;

        /**
         * 弹出窗口宽度
         */
        String windowWidth = "960px";

        /**
         * 弹出窗口高度
         */
        String windowHeight = "800px";

        /**
         * 模板渲染类
         */
        Class<? extends BaseTplRender> tplClazz = BaseTplRender.class;


        @Override
        public Class<? extends BaseTplRender> tplClazz() {
            return this.tplClazz;
        }

        @Override
        public String windowWidth() {
            return this.windowWidth;
        }

        @Override
        public String windowHeight() {
            return this.windowHeight;
        }

        @Override
        public boolean enable() {
            return this.enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return AnnoTpl.class;
        }
    }
}