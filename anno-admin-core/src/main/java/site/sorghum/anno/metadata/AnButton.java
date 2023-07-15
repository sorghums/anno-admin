package site.sorghum.anno.metadata;

import lombok.Data;

/**
 * 按钮信息
 *
 * @author songyinyin
 * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton
 * @since 2023/7/9 22:24
 */
@Data
public class AnButton {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 标题
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton#name()
     */
    private String name;

    /**
     * 按钮大小 	'xs' | 'sm' | 'md' | 'lg'
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton#size()
     */
    private String size;

    /**
     * 按下按钮后的js命令
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton#jsCmd()
     */
    private String jsCmd;

    /**
     * 跳转的url
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton#jumpUrl()
     */
    private String jumpUrl;

    /**
     * 一对多：是否启用
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.O2MJoinButton#enable()
     */
    private boolean o2mEnable;

    /**
     * 一对多：连表查询
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.O2MJoinButton#joinAnnoMainClazz()
     */
    private Class<?> o2mJoinMainClazz;

    /**
     * 一对多：以哪个字段为条件【this】
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.O2MJoinButton#joinThisClazzField()
     */
    private String o2mJoinThisField;

    /**
     * 一对多：以哪个字段为条件【target】
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.O2MJoinButton#joinOtherClazzField()
     */
    private String o2mJoinOtherField;

    /**
     * 多对多是否启用
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#enable()
     */
    private boolean m2mEnable;

    /**
     * 多对多：目标表
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#joinAnnoMainClazz()
     */
    private Class<?> m2mJoinAnnoMainClazz;

    /**
     * 多对多：连表 sql
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#joinSql()
     */
    private String m2mJoinSql;

    /**
     * 多对多：以哪个字段为条件【this】
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#joinThisClazzField()
     */
    private String m2mJoinThisClazzField;

    /**
     * 多对多：中间表的类
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#mediumTableClass()
     */
    private Class<?> m2mMediumTableClass;

    /**
     * 多对多：中间表的字段【目标表】
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#mediumOtherField()
     */
    private String m2mMediumOtherField;

    /**
     * 多对多：中间表的字段【本表】
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.M2MJoinButton#mediumThisField()
     */
    private String m2mMediumThisField;

    /**
     * JavaCmd：启用
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.JavaCmd#enable()
     */
    private boolean javaCmdEnable;

    /**
     * JavaCmd：bean类
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.JavaCmd#beanClass()
     */
    private Class<?> javaCmdBeanClass;

    /**
     * JavaCmd：方法名
     *
     * @see site.sorghum.anno.modular.anno.annotation.field.AnnoButton.JavaCmd#methodName()
     */
    private String javaCmdMethodName;


}
