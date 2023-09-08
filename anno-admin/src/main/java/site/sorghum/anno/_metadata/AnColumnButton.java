package site.sorghum.anno._metadata;

import lombok.Data;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;

/**
 * 行级按钮信息
 *
 * @author songyinyin
 * @see AnnoButton
 * @since 2023/7/9 22:24
 */
@Data
public class AnColumnButton {

    /**
     * 字段名
     */
    private String fieldName;

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
     * 一对多：是否启用
     *
     * @see AnnoButton.O2MJoinButton#enable()
     */
    private boolean o2mEnable;

    /**
     * 一对多：连表查询
     *
     * @see AnnoButton.O2MJoinButton#joinAnnoMainClazz()
     */
    private Class<?> o2mJoinMainClazz;

    /**
     * 一对多：以哪个字段为条件【this】
     *
     * @see AnnoButton.O2MJoinButton#joinThisClazzField()
     */
    private String o2mJoinThisField;

    /**
     * 一对多：以哪个字段为条件【target】
     *
     * @see AnnoButton.O2MJoinButton#joinOtherClazzField()
     */
    private String o2mJoinOtherField;

    /**
     * 一对多：窗口大小
     *
     * @see AnnoButton.O2MJoinButton#windowSize()
     */
    private String o2mWindowSize;

    /**
     * 一对多：窗口高度
     *
     * @see AnnoButton.O2MJoinButton#windowHeight()
     */
    private String o2mWindowHeight;

    /**
     * 多对多是否启用
     *
     * @see AnnoButton.M2MJoinButton#enable()
     */
    private boolean m2mEnable;

    /**
     * 多对多：目标表
     *
     * @see AnnoButton.M2MJoinButton#joinAnnoMainClazz()
     */
    private Class<?> m2mJoinAnnoMainClazz;

    /**
     * 多对多：连表 sql
     *
     * @see AnnoButton.M2MJoinButton#joinSql()
     */
    private String m2mJoinSql;

    /**
     * 多对多：以哪个字段为条件【this】
     *
     * @see AnnoButton.M2MJoinButton#joinThisClazzField()
     */
    private String m2mJoinThisClazzField;

    /**
     * 多对多：中间表的类
     *
     * @see AnnoButton.M2MJoinButton#mediumTableClass()
     */
    private Class<?> m2mMediumTableClass;

    /**
     * 多对多：中间表的字段【目标表】
     *
     * @see AnnoButton.M2MJoinButton#mediumOtherField()
     */
    private String m2mMediumOtherField;

    /**
     * 多对多：中间表的字段【本表】
     *
     * @see AnnoButton.M2MJoinButton#mediumThisField()
     */
    private String m2mMediumThisField;

    /**
     * 多对多：窗口大小
     *
     * @see AnnoButton.M2MJoinButton#windowSize()
     */
    private String m2mWindowSize;

    /**
     * 多对多：窗口高度
     *
     * @see AnnoButton.M2MJoinButton#windowHeight()
     */
    private String m2mWindowHeight;

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
     * 模板资源名称
     *
     * @see AnnoTpl#tplName()
     */
    private String tplName;

    /**
     * 模板资源类
     *
     * @see AnnoTpl#tplClazz()
     */
    private Class<?> tplClazz;

    /**
     * 模板资源是否启用
     */
    private boolean tplEnable;

    /**
     * 按钮权限码
     *
     * @see AnnoButton#permissionCode()
     */
    private String permissionCode;
}
