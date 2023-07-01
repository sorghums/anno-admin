package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class Video extends AmisBase {
    {
        setType("video");
    }
    //src	string		视频地址
    //isLive	boolean	false	是否为直播，视频为直播时需要添加上，支持flv和hls格式
    //videoType	string		指定直播视频格式
    //poster	string		视频封面地址
    //muted	boolean		是否静音
    //loop	boolean		是否循环播放
    //autoPlay	boolean		是否自动播放
    //rates	array		倍数，格式为[1.0, 1.5, 2.0]
    //frames	object		key 是时刻信息，value 可以可以为空，可有设置为图片地址，请看上方示例
    //jumpBufferDuration	boolean		点击帧的时候默认是跳转到对应的时刻，如果想提前 3 秒钟，可以设置这个值为 3
    //stopOnNextFrame	boolean		到了下一帧默认是接着播放，配置这个会自动停止

    /**
     * 视频地址
     */
    String src;

    /**
     * 是否为直播，视频为直播时需要添加上，支持flv和hls格式
     */
    Boolean isLive;

    /**
     * 指定直播视频格式
     */
    String videoType;

    /**
     * 视频封面地址
     */
    String poster;

    /**
     * 是否静音
     */
    Boolean muted;

    /**
     * 是否循环播放
     */
    Boolean loop;

    /**
     * 是否自动播放
     */
    Boolean autoPlay;

    /**
     * 倍数，格式为[1.0, 1.5, 2.0]
     */
    List<String> rates;

    /**
     * key 是时刻信息，value 可以可以为空，可有设置为图片地址，请看上方示例
     */
    Map<String,Object> frames;

    /**
     * 点击帧的时候默认是跳转到对应的时刻，如果想提前 3 秒钟，可以设置这个值为 3
     */
    Boolean jumpBufferDuration;

    /**
     * 到了下一帧默认是接着播放，配置这个会自动停止
     */
    Boolean stopOnNextFrame;
}
