package site.sorghum.anno._metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class AnColumnButton extends AnButton {

    /**
     * 字段名
     */
    private String fieldName;

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
     * 模板资源窗口大小
     * xs、sm、md、lg、xl、full
     */
    private String tplWindowSize;

    /**
     * 模板资源窗口高度
     */
    private String tplWindowHeight;

    /**
     * 模板资源是否启用
     */
    private boolean tplEnable;
}
