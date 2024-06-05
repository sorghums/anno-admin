package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.tpl.BaseTplRender;

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
    private Boolean o2mEnable;

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
     * 多对多是否启用
     *
     * @see AnnoButton.M2MJoinButton#enable()
     */
    @ApiModelProperty(value = "多对多是否启用",example = "true")
    private Boolean m2mEnable;

    /**
     * 多对多的数据
     *
     * @see AnnoMtm
     */
    @ApiModelProperty(value = "多对多的数据")
    private AnnoMtm m2mData;

    /**
     * 模板资源名称
     *
     * @see AnnoField#title()
     */
    @ApiModelProperty(value = "模板资源名称",example = "tplName")
    private String tplName;

    /**
     * 模板资源ID
     */
    @ApiModelProperty(value = "模板资源ID",example = "A:a123456")
    private String tplId;

    /**
     * 模板资源类
     *
     * @see AnnoTpl#tplClazz()
     */
    @ApiModelProperty(value = "模板资源类",example = "site.sorghum.anno._metadata.AnEntity")
    private Class<? extends BaseTplRender> tplClazz;

    /**
     * 模板资源窗口大小
     * xs、sm、md、lg、xl、full
     */
    @ApiModelProperty(value = "模板资源窗口大小",example = "md")
    private String tplWindowWidth;

    /**
     * 模板资源窗口高度
     */
    @ApiModelProperty(value = "模板资源窗口高度",example = "md")
    private String tplWindowHeight;

    /**
     * 模板资源是否启用
     */
    @ApiModelProperty(value = "模板资源是否启用",example = "true")
    private Boolean tplEnable;
}
