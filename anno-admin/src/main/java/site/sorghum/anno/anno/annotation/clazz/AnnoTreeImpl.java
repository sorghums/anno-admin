package site.sorghum.anno.anno.annotation.clazz;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * Anno 树结构注解
 * 用于标注树结构的注解
 *
 * @author sorghum
 * @since 2023/05/21
 */
@Data
public class AnnoTreeImpl implements AnnoTree {
    /**
     * 标签
     */
    private String label = "";

    /**
     * 父节点关键词
     */
    private String parentKey = "";

    /**
     * 节点关键词
     */
    private String key= "";

    /**
     * 是否显示为树
     */
    private boolean displayAsTree;

    /**
     * 是否启用
     */
    private boolean enable = true;

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public String parentKey() {
        return this.parentKey;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public boolean displayAsTree() {
        return this.displayAsTree;
    }

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoTree.class;
    }
}