package site.sorghum.anno._metadata;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;

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
     * @see AnnoTableButton#name()
     */
    private String name;

    /**
     * 按钮大小 	'xs' | 'sm' | 'md' | 'lg'
     *
     * @see AnnoTableButton#size()
     */
    private String size;

    /**
     * 按下按钮后的js命令
     *
     * @see AnnoTableButton#jsCmd()
     */
    private String jsCmd;

    /**
     * 跳转的url
     *
     * @see AnnoTableButton#jumpUrl()
     */
    private String jumpUrl;


    /**
     * JavaCmd：启用
     *
     * @see AnnoTableButton.JavaCmd#enable()
     */
    private boolean javaCmdEnable;

    /**
     * JavaCmd：bean类
     *
     * @see AnnoTableButton.JavaCmd#beanClass()
     */
    private Class<?> javaCmdBeanClass;

    /**
     * JavaCmd：方法名
     *
     * @see AnnoTableButton.JavaCmd#methodName()
     */
    private String javaCmdMethodName;


    /**
     * 按钮权限码
     *
     * @see AnnoTableButton#permissionCode()
     */
    private String permissionCode;
}
