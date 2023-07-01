package site.sorghum.amis.entity.display;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.List;
import java.util.Map;

/**
 * IFrame
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IFrame extends AmisBase {
    {
        setType("iframe");
    }
    //frameBorder	Array		frameBorder
    //style	object		样式对象
    //src	string		iframe 地址
    //allow	string		allow 配置
    //sandbox	string		sandbox 配置
    //referrerpolicy	string		referrerpolicy 配置
    //height	number或string	"100%"	iframe 高度
    //width	number或string	"100%"	iframe 宽度

    /**
     * frameBorder
     */
    List<String> frameBorder;

    /**
     * 样式对象
     */
    Map<String,Object> style;

    /**
     * iframe 地址
     */
    String src;

    /**
     * allow 配置
     */
    String allow;

    /**
     * sandbox 配置
     */
    String sandbox;

    /**
     * referrerPolicy 配置
     */
    @JSONField(name = "referrerpolicy")
    String referrerPolicy;

    /**
     * iframe 高度
     */
    String height;

    /**
     * iframe 宽度
     */
    String width;


}
