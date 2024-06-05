package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;

/**
 * 按钮信息
 *
 * @author Sorghum
 * @since 2023/08/08
 */
@Data
public class AnButton {

    /**
     * 标题
     *
     * @see AnnoTableButton#name()
     */
    @ApiModelProperty(value = "标题", example = "叽里呱啦")
    private String name;

    /**
     * 标题
     *
     * @see AnnoTableButton#icon()
     */
    @ApiModelProperty(value = "图标", example = "ant-design:appstore-filled")
    private String icon;

    /**
     * 按钮大小 	'default' | 'middle' | 'small' | 'large'
     *
     * @see AnnoTableButton#size()
     */
    @ApiModelProperty(value = "按钮大小 	'default' | 'middle' | 'small' | 'large'", example = "default")
    private String size;

    /**
     * 按下按钮后的js命令
     *
     * @see AnnoTableButton#jsCmd()
     */
    @ApiModelProperty(value = "按下按钮后的js命令", example = "alert('hello world')")
    private String jsCmd;

    /**
     * 跳转的url
     *
     * @see AnnoTableButton#jumpUrl()
     */
    @ApiModelProperty(value = "跳转的url", example = "/user/add")
    private String jumpUrl;


    /**
     * JavaCmd：启用
     *
     * @see AnnoTableButton.JavaCmd#enable()
     */
    @ApiModelProperty(value = "JavaCmd：启用", example = "true")
    private Boolean javaCmdEnable;

    /**
     * JavaCmd：bean类
     *
     * @see AnnoTableButton.JavaCmd
     */
    @ApiModelProperty(value = "JavaCmd数据", example = "b12345")
    private AnnoJavaCmd javaCmdData;


    /**
     * 按钮权限码
     *
     * @see AnnoTableButton#permissionCode()
     */
    @ApiModelProperty(value = "按钮权限码", example = "anno_mine_permission_user_button")
    private String permissionCode;
}
