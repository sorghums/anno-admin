package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "字段名",example = "id")
    private String fieldName;

    /**
     * 一对多：是否启用
     *
     * @see AnnoButton.O2MJoinButton#enable()
     */
    @ApiModelProperty(value = "一对多：是否启用",example = "true")
    private boolean o2mEnable;

    /**
     * 一对多：连表查询
     *
     * @see AnnoButton.O2MJoinButton#targetClass()
     */
    @ApiModelProperty(value = "一对多：连表查询",example = "site.sorghum.anno._metadata.AnEntity")
    private Class<?> o2mTargetClass;

    /**
     * 一对多：以哪个字段为条件【this】
     *
     * @see AnnoButton.O2MJoinButton#thisJavaField()
     */
    @ApiModelProperty(value = "一对多：以哪个字段为条件【this】",example = "id")
    private String o2mThisJavaField;

    /**
     * 一对多：以哪个字段为条件【target】
     *
     * @see AnnoButton.O2MJoinButton#targetJavaField()
     */
    @ApiModelProperty(value = "一对多：以哪个字段为条件【target】",example = "id")
    private String o2mTargetJavaField;

    /**
     * 一对多：窗口大小
     *
     * @see AnnoButton.O2MJoinButton#windowSize()
     */
    @ApiModelProperty(value = "一对多：窗口大小",example = "md")
    private String o2mWindowSize;

    /**
     * 一对多：窗口高度
     *
     * @see AnnoButton.O2MJoinButton#windowHeight()
     */
    @ApiModelProperty(value = "一对多：窗口高度",example = "md")
    private String o2mWindowHeight;

    /**
     * 多对多是否启用
     *
     * @see AnnoButton.M2MJoinButton#enable()
     */
    @ApiModelProperty(value = "多对多是否启用",example = "true")
    private boolean m2mEnable;

    /**
     * 多对多：目标表
     *
     * @see AnnoButton.M2MJoinButton#joinTargetClazz()
     */
    @ApiModelProperty(value = "多对多：目标表",example = "site.sorghum.anno._metadata.AnEntity")
    private Class<?> m2mJoinTargetClazz;

    /**
     * 多对多：连表 sql
     *
     * @see AnnoButton.M2MJoinButton#joinSql()
     */
    @ApiModelProperty(value = "多对多：连表 sql",example = "e12345")
    private String m2mJoinSql;

    /**
     * 多对多：以哪个字段为条件【this】
     *
     * @see AnnoButton.M2MJoinButton#joinThisClazzField()
     */
    @ApiModelProperty(value = "多对多：以哪个字段为条件【this】",example = "id")
    private String m2mJoinThisClazzField;

    /**
     * 多对多：以哪个字段为条件【target】
     *
     * @see AnnoButton.M2MJoinButton#joinTargetClazzField()
     */
    @ApiModelProperty(value = "多对多：以哪个字段为条件【target】",example = "id")
    private String m2mJoinTargetClazzField;

    /**
     * 多对多：中间表的类
     *
     * @see AnnoButton.M2MJoinButton#mediumTableClass()
     */
    @ApiModelProperty(value = "多对多：中间表的类",example = "site.sorghum.anno._metadata.AnEntity")
    private Class<?> m2mMediumTableClass;

    /**
     * 多对多：中间表的字段【目标表】
     *
     * @see AnnoButton.M2MJoinButton#mediumTargetField()
     */
    @ApiModelProperty(value = "多对多：中间表的字段【目标表】",example = "ta_id")
    private String m2mMediumTargetField;

    /**
     * 多对多：中间表的字段【本表】
     *
     * @see AnnoButton.M2MJoinButton#mediumThisField()
     */
    @ApiModelProperty(value = "多对多：中间表的字段【本表】",example = "th_id")
    private String m2mMediumThisField;

    /**
     * 多对多：窗口大小
     *
     * @see AnnoButton.M2MJoinButton#windowSize()
     */
    @ApiModelProperty(value = "多对多：窗口大小",example = "md")
    private String m2mWindowSize;

    /**
     * 多对多：窗口高度
     *
     * @see AnnoButton.M2MJoinButton#windowHeight()
     */
    @ApiModelProperty(value = "多对多：窗口高度",example = "md")
    private String m2mWindowHeight;

    /**
     * 模板资源名称
     *
     * @see AnnoTpl#tplName()
     */
    @ApiModelProperty(value = "模板资源名称",example = "tplName")
    private String tplName;

    /**
     * 模板资源类
     *
     * @see AnnoTpl#tplClazz()
     */
    @ApiModelProperty(value = "模板资源类",example = "site.sorghum.anno._metadata.AnEntity")
    private Class<?> tplClazz;

    /**
     * 模板资源窗口大小
     * xs、sm、md、lg、xl、full
     */
    @ApiModelProperty(value = "模板资源窗口大小",example = "md")
    private String tplWindowSize;

    /**
     * 模板资源窗口高度
     */
    @ApiModelProperty(value = "模板资源窗口高度",example = "md")
    private String tplWindowHeight;

    /**
     * 模板资源是否启用
     */
    @ApiModelProperty(value = "模板资源是否启用",example = "true")
    private boolean tplEnable;
}
