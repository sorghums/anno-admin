package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.solon.i18n.I18nUtil;
import site.sorghum.amis.entity.function.Api;

/**
 * 输入图像
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputImage extends FormItem{
    {
        setType("input-image");
    }

    /**
     * 上传文件接口
     */
    Api receiver;

    /**
     * 支持的图片类型格式，请配置此属性为图片后缀，例如.jpg,.png
     */
    String accept = ".jpeg,.jpg,.png,.gif";

    /**
     * 默认没有限制，当设置后，文件大小大于此值将不允许上传。单位为B
     */
    Integer maxSize;

    /**
     * 默认没有限制，当设置后，一次只允许上传指定数量文件。
     */
    Integer maxLength;

    /**
     * 是否多选。
     */
    Boolean multiple = false;

    /**
     * 是否拼接value值
     */
    Boolean joinValues = true;

    /**
     * 是否将value值抽取出来组成新的数组，只有在joinValues是false是生效
     */
    Boolean extractValue = false;

    /**
     * 	拼接符
     */
    String delimiter = ",";

    /**
     * 否选择完就自动开始上传
     */
    Boolean autoUpload = true;

    /**
     * 隐藏上传按钮
     */
    Boolean hideUploadButton = false;

    /**
     * 如果你不想自己存储，则可以忽略此属性。
     */
    String fileField = "file";

    /**
     * 用来设置是否支持裁剪。
     * crop.aspectRatio	Number	裁剪比例，比如 1 表示 1:1，0.75 表示 4:3，默认为 1。
     * crop.rotatable	Boolean	是否支持旋转，默认为 true。
     * crop.scalable	Boolean	是否支持缩放，默认为 true。
     * crop.viewMode	Number	裁剪框的显示模式，可选值为 0、1、2、3，默认为 0。
     */
    Object crop = false;

    /**
     * 裁剪文件格式
     */
    String cropFormat = "image/png";

    /**
     * 裁剪文件格式的质量，用于 jpeg/webp，取值在 0 和 1 之间
     */
    Float cropQuality = 1f;

    /**
     * 限制图片大小，超出不让上传。
     */
    Limit limit;

    /**
     * 默认占位图地址
     */
    String frameImage;

    /**
     * 是否开启固定尺寸,若开启，需同时设置 fixedSizeClassName
     */
    Boolean fixedSize;

    /**
     * 开启固定尺寸时，根据此值控制展示尺寸。
     * 例如h-30,即图片框高为 h-30,AMIS 将自动缩放比率设置默认图所占位置的宽度，最终上传图片根据此尺寸对应缩放。
     */
    String fixedSizeClassName;

    /**
     * 表单反显时是否执行 autoFill
     */
    Boolean initAutoFill = true;

    /**
     * 上传按钮文案。支持tpl、schema形式配置。
     */
    String uploadButtonLabel = I18nUtil.getMessage("amis.input-image.upload-btn");

    /**
     * 图片上传后是否进入裁剪模式
     */
    Boolean dropCrop = true;

    /**
     * 图片选择器初始化后是否立即进入裁剪模式
     */
    Boolean initCrop = false;

    @Data
    public static class Limit{
        /**
         * 限制图片宽度。
         */
        Integer width;

        /**
         * 限制图片高度。
         */
        Integer height;

        /**
         * 限制图片最小宽度。
         */
        Integer minWidth;

        /**
         * 限制图片最小高度。
         */
        Integer minHeight;

        /**
         * 限制图片最大宽度。
         */
        Integer maxWidth;

        /**
         * 限制图片最大高度。
         */
        Integer maxHeight;

        /**
         * 限制图片宽高比，格式为浮点型数字，默认 1 即 1:1，如果要设置 16:9 请设置 1.7777777777777777 即 16 / 9。 如果不想限制比率，请设置空字符串。
         */
        Float aspectRatio;

    }

    @Data
    public static class Crop {
        /**
         * 裁剪比例，比如 1 表示 1:1，0.75 表示 4:3，默认为 1。
         */
        Float aspectRatio;

        /**
         * 是否支持旋转，默认为 true。
         */
        Boolean rotatable;

        /**
         * 是否支持缩放，默认为 true。
         */
        Boolean scalable;

        /**
         * 裁剪框的显示模式，可选值为 0、1、2、3，默认为 0。
         */
        Integer viewMode;

    }

}
