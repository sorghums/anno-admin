package site.sorghum.anno.amis.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.TemplateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
public class AmisBaseController {

    @Inject
    protected MetadataManager metadataManager;

    public AnnoResult<Object> toJson(String clazz, Map<String, Object> data, boolean isM2m) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Object template = null;
        Map<String, Object> properties = new HashMap<String, Object>() {{
            put("clazz", clazz);
            put("treeClazz", AnnoUtil.getTreeClass(anEntity.getClazz()));
            this.putAll(data);
            put("extraData", JSON.toJSONString(data));
        }};
        if (!isM2m) {
            // 添加树类
            if (anEntity.isEnableTree() && anEntity.isTreeDisplayAsTree()) {
                template = TemplateUtil.getTreeTemplate(anEntity.getClazz(), properties);
            }
            // 添加crud类
            if (template == null) {
                template = TemplateUtil.getCrudTemplate(anEntity.getClazz(), properties);
            }
        } else {
            // 添加树类
            if (anEntity.isEnableTree() && anEntity.isTreeDisplayAsTree()) {
                template = TemplateUtil.getTreeM2mTemplate(anEntity.getClazz(), properties);
            }
            // 添加crud类
            if (template == null) {
                template = TemplateUtil.getCrudM2mTemplate(anEntity.getClazz(), properties);
            }
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("amisJSON", JSONUtil.toBean(template, HashMap.class));
        returnMap.put("properties", properties);
        return AnnoResult.succeed(returnMap);
    }

}
