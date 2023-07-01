package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 条形码
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BarCode extends AmisBase {
    {
        setType("barcode");
    }
    //value	string		显示的颜色值

    /**
     * 条形码内容
     */
    String value;

    /**
     * 在其他组件中，时，用作变量映射
     */
    String type;

    /**
     * 条形码格式
     */
    BarCodeOptions options;

    @Data
    public static class BarCodeOptions {
    	/**
    	 * 条形码格式
    	 */
    	String format;
    	/**
    	 * 条形码颜色
    	 */
    	String lineColor;
    	/**
    	 * 条形码宽度
    	 */
    	String width;
    	/**
    	 * 条形码高度
    	 */
    	String height;
    }


}
