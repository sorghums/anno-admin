package site.sorghum.anno.anno.tpl;

import lombok.Data;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._common.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * TPL渲染
 *
 * @author Sorghum
 * @since 2023/12/21
 */
@Data
public class TplRender {
    /**
     * tpl渲染图
     */
    public static Map<String,TplRender> tplRenderMap = new HashMap<>();
    /**
     * id
     */
    private String id = null;
    /**
     * view对象
     */
    private String view = null;

    /**
     * 参数
     */
    private final Map<String,Object> props = new HashMap<>();

    /**
     * 初始化
     *
     * @param view  view对象
     * @param props 支柱
     */
    public void init(String view, Map<String, Object> props){
        this.view = view;
        this.props.putAll(props);
        this.id =toId(this.getClass());
        put(this.id,this);
    }

    public static String toId(Class<? extends TplRender> clazz){
        return clazz.getSimpleName() + ":" + MD5Util.digestHex(clazz.getName());
    }

    public static void put(String key, TplRender tplRender) {
        tplRenderMap.put(key, tplRender);
    }

    public static TplRender getClone(String key){
        if (tplRenderMap.get(key) == null){
            return null;
        }
        return JSONUtil.copy(tplRenderMap.get(key));
    }

}
