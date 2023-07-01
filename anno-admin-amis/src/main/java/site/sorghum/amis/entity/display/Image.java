package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 图像
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Image extends AmisBase {
    {
        setType("image");
    }
    //innerClassName	string		组件内层 CSS 类名
    //imageClassName	string		图片 CSS 类名
    //thumbClassName	string		图片缩率图 CSS 类名
    //height	string		图片缩率高度
    //width	string		图片缩率宽度
    //title	string		标题
    //imageCaption	string		描述
    //placeholder	string		占位文本
    //defaultImage	string		无数据时显示的图片
    //src	string		缩略图地址
    //href	模板		外部链接地址
    //originalSrc	string		原图地址
    //enlargeAble	boolean		支持放大预览
    //enlargeTitle	string		放大预览的标题
    //enlargeCaption	string		放大预览的描述
    //thumbMode	string	contain	预览图模式，可选：'w-full', 'h-full', 'contain', 'cover'
    //thumbRatio	string	1:1	预览图比例，可选：'1:1', '4:3', '16:9'
    //imageMode	string	thumb	图片展示模式，可选：'thumb', 'original' 即：缩略图模式 或者 原图模式
    //showToolbar	boolean	false	放大模式下是否展示图片的工具栏	2.2.0
    //toolbarActions	ImageAction[]		图片工具栏，支持旋转，缩放，默认操作全部开启

    /**
     * 组件内层 CSS 类名
     */
    String innerClassName;

    /**
     * 图片 CSS 类名
     */
    String imageClassName;

    /**
     * 图片缩率图 CSS 类名
     */
    String thumbClassName;

    /**
     * 图片缩率高度
     */
    String height;

    /**
     * 图片缩率宽度
     */
    String width;

    /**
     * 标题
     */
    String title;

    /**
     * 描述
     */
    String imageCaption;

    /**
     * 占位文本
     */
    String placeholder;

    /**
     * 无数据时显示的图片
     */
    String defaultImage;

    /**
     * 缩略图地址
     */
    String src;

    /**
     * 外部链接地址
     */
    String href;

    /**
     * 原图地址
     */
    String originalSrc;

    /**
     * 支持放大预览
     */
    Boolean enlargeAble;

    /**
     * 放大预览的标题
     */
    String enlargeTitle;

    /**
     * 放大预览的描述
     */
    String enlargeCaption;

    /**
     * 预览图模式，可选：'w-full', 'h-full', 'contain', 'cover'
     */
    String thumbMode;

    /**
     * 预览图比例，可选：'1:1', '4:3', '16:9'
     */
    String thumbRatio;

    /**
     * 图片展示模式，可选：'thumb', 'original' 即：缩略图模式 或者 原图模式
     */
    String imageMode;

    /**
     * 放大模式下是否展示图片的工具栏	2.2.0
     */
    Boolean showToolbar;

    /**
     * 图片工具栏，支持旋转，缩放，默认操作全部开启
     */
    ImageAction[] toolbarActions;

    /**
     * 图片工具栏，支持旋转，缩放，默认操作全部开启
     */
    @Data
    public static class ImageAction{
        /**
         * 操作key
         * rotateRight | rotateLeft | zoomIn | zoomOut | scaleOrigin
         */
        String key;

        /**
         * 动作名称
         */
        String label;

        /**
         * 动作icon
         */
        String icon;

        /**
         * 动作自定义CSS类
         */
        String iconClassName;

        /**
         * 动作是否禁用
         */
        Boolean disabled;

    }

}
