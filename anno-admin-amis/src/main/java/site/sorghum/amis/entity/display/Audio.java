package site.sorghum.amis.entity.display;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.List;

/**
 * 音频
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Audio extends AmisBase {
    {
        setType("audio");
    }
    //inline	boolean	true	是否是内联模式
    //src	string		音频地址
    //loop	boolean	false	是否循环播放
    //autoPlay	boolean	false	是否自动播放
    //rates	array	[]	可配置音频播放倍速如：[1.0, 1.5, 2.0]
    //controls	array	['rates', 'play', 'time', 'process', 'volume']	内部模块定制化

    /**
     * 是否是内联模式
     */
    Boolean inline = true;

    /**
     * 音频地址
     */
    String src;

    /**
     * 是否循环播放
     */
    Boolean loop = false;

    /**
     * 是否自动播放
     */
    Boolean autoPlay = false;

    /**
     * 可配置音频播放倍速如：[1.0, 1.5, 2.0]
     */
    List<Double> rates;

    /**
     * 内部模块定制化
     * 默认：['rates', 'play', 'time', 'process', 'volume']
     */
    List<String> controls = CollUtil.newArrayList("rates", "play", "time", "process", "volume");

}
