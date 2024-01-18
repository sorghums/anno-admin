package site.sorghum.anno._metadata;

import site.sorghum.anno.method.MethodTemplate;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/15 14:01
 */
@MethodTemplate(ruleDir = "metadata")
public interface MetadataContext {

    /**
     * 加载所有元数据后，会调用此方法，可以用 allEntities 做一些结构化的扩展
     */
    void refresh(List<AnEntity> allEntities);
}
