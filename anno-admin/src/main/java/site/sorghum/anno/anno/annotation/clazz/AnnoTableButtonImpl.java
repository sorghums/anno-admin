package site.sorghum.anno.anno.annotation.clazz;

import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.lang.annotation.Annotation;

/**
 * Anno Table Button 注解实现类
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class AnnoTableButtonImpl implements AnnoTableButton {
    /**
     * 按钮名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon = "ant-design:appstore-filled";

    /**
     * 按钮大小 'default' | 'middle' | 'small' | 'large'
     */
    private String size = "default";

    /**
     * 表单提供类
     */
    private Class<? extends BaseForm> baseForm = DefaultBaseForm.class;

    /**
     * 按下按钮后的js命令
     */
    private String jsCmd = "";

    /**
     * 跳转的url
     */
    private String jumpUrl = "";

    /**
     * java命令行
     */
    private JavaCmdImpl javaCmd = new JavaCmdImpl();

    /**
     * 权限码，默认为空，不进行权限控制
     */
    private String permissionCode = "";

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String icon() {
        return this.icon;
    }

    @Override
    public String size() {
        return this.size;
    }

    @Override
    public Class<? extends BaseForm> baseForm() {
        return this.baseForm;
    }

    @Override
    public String jsCmd() {
        return this.jsCmd;
    }

    @Override
    public String jumpUrl() {
        return this.jumpUrl;
    }

    @Override
    public JavaCmd javaCmd() {
        return this.javaCmd;
    }

    @Override
    public String permissionCode() {
        return this.permissionCode;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoTableButton.class;
    }

    /**
     * Java命令行的内部类实现
     */
    public class JavaCmdImpl implements JavaCmd {
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
            return this.runSupplier;
        }

        @Override
        public boolean enable() {
            return this.enable;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return JavaCmd.class;
        }
    }
}