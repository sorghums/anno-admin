package site.sorghum.anno.anno.annotation.field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.snack.annotation.ONodeAttr;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.common.AnnoTplImpl;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.lang.annotation.Annotation;

/**
 * Anno Button 注解
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private O2MJoinButtonImpl o2mJoinButton = O2MJoinButtonImpl.builder().enable(false).build();


    /**
     * 多对多关联按钮
     */
    private M2MJoinButtonImpl m2mJoinButton = M2MJoinButtonImpl.builder().enable(false).build();

    /**
     * java命令行
     */
    private JavaCmdImpl javaCmd = new JavaCmdImpl();

    /**
     * 模板视图按钮
     */
    private AnnoTplImpl annoTpl = AnnoTplImpl.builder().enable(false).build();

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
    public M2MJoinButton m2mJoinButton() {
        return m2mJoinButton;
    }

    @Override
    public JavaCmd javaCmd() {
        return javaCmd;
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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
     * 多对多关联按钮的内部类
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class M2MJoinButtonImpl implements M2MJoinButton {
        /**
         * 多对多缓存主键
         */
        private String id;

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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JavaCmdImpl implements JavaCmd {

        /**
         * 缓存主键
         */
        private String id;

        /**
         * 运行供应商
         */
        private Class<? extends JavaCmdSupplier> runSupplier = JavaCmdSupplier.class;

        /**
         * 启用
         */
        private boolean enable = false;

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

}