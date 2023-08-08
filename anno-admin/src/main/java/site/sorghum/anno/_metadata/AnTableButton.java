package site.sorghum.anno._metadata;

import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoButton;

/**
 * 按钮信息
 *
 * @author Sorghum
 * @since 2023/08/08
 */
@Data
public class AnTableButton {

    /**
     * 标题
     *
     * @see AnnoButton#name()
     */
    private String name;

    /**
     * 按钮大小 	'xs' | 'sm' | 'md' | 'lg'
     *
     * @see AnnoButton#size()
     */
    private String size;

    /**
     * 按下按钮后的js命令
     *
     * @see AnnoButton#jsCmd()
     */
    private String jsCmd;

    /**
     * 跳转的url
     *
     * @see AnnoButton#jumpUrl()
     */
    private String jumpUrl;


    /**
     * JavaCmd：启用
     *
     * @see AnnoButton.JavaCmd#enable()
     */
    private boolean javaCmdEnable;

    /**
     * JavaCmd：bean类
     *
     * @see AnnoButton.JavaCmd#beanClass()
     */
    private Class<?> javaCmdBeanClass;

    /**
     * JavaCmd：方法名
     *
     * @see AnnoButton.JavaCmd#methodName()
     */
    private String javaCmdMethodName;


    /**
     * 按钮权限码
     *
     * @see AnnoButton#permissionCode()
     */
    private String permissionCode;
}
