package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 二维码
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QrCode extends AmisBase {
    {
        setType("qr-code");
    }
    /**
     * 二维码 SVG 的类名
     */
    String qrcodeClassName;

    /**
     * 二维码的宽高大小
     */
    Integer codeSize = 128;

    /**
     * 二维码背景色
     */
    String backgroundColor = "#fff";

    /**
     * 二维码前景色
     */
    String foregroundColor = "#000";

    /**
     * 二维码复杂级别，有（'L' 'M' 'Q' 'H'）四种
     */
    String level = "L";

    /**
     * 扫描二维码后显示的文本，如果要显示某个页面请输入完整 url（"http://..."或"https://..."开头），支持使用 模板
     */
    String value = "https://www.baidu.com";

    /**
     * QRCode 图片配置
     */
    ImageSettings imageSettings;

    @Data
    public static class ImageSettings{
        /**
         * 图片链接地址
         */
        String src;

        /**
         * 图片宽度
         */
        Integer width;

        /**
         * 图片高度
         */
        Integer height;

        /**
         * 图片水平方向偏移量
         */
        Integer x;

        /**
         * 图片垂直方向偏移量
         */
        Integer y;

        /**
         * 图片是否挖孔嵌入
         */
        Boolean excavate;
    }
}
