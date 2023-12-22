package site.sorghum.anno.anno.tpl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * TPL渲染
 *
 * @author Sorghum
 * @since 2023/12/22
 */
@Data
@Slf4j
public abstract class BaseTplRender {
    /**
     * tpl渲染图
     */
    public static Map<String, BaseTplRender> tplRenderMap = new HashMap<>();
    /**
     * id
     */
    private String id = null;
    /**
     * 参数
     */
    private Map<String, Object> props = new HashMap<>();

    public BaseTplRender() {
        this.id = toId(this.getClass());
        // 防止重复注册
        if (!tplRenderMap.containsKey(this.id)) {
            put(this.id, this);
        }
    }

    /**
     * 初始化
     *
     * @param props 支柱
     */
    public void addProps(Map<String, Object> props) {
        this.props.putAll(props);
    }

    /**
     * 获取视图名称
     *
     * @return {@link String}
     */
    public abstract String getViewName();

    /**
     * 钩
     */
    public abstract void hook();

    /**
     * 获取令牌
     *
     * @return {@link String}
     */
    public String getToken() {
        return MapUtil.getStr(this.props, "_tokenValue");
    }


    public static String toId(Class<? extends BaseTplRender> clazz) {
        return clazz.getSimpleName();
    }

    private static void put(String key, BaseTplRender baseTplRender) {
        tplRenderMap.put(key, baseTplRender);
    }

    public static BaseTplRender getClone(String key, String className) {
        if (tplRenderMap.containsKey(key)) {
            return JSONUtil.copy(tplRenderMap.get(key));
        }
        if (StrUtil.isNotBlank(className)) {
            try {
                return JSONUtil.copy(AnnoBeanUtils.getBean(ClassUtil.loadClass(className)));
            } catch (Exception e) {
                log.error("通过className获取TplRender失败,error:{}", e.getMessage());
            }
        }
        log.error("获取TplRender失败,key:{},className:{}", key, className);
        return null;
    }
}
