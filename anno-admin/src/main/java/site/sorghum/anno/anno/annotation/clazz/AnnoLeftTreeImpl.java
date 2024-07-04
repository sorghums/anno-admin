package site.sorghum.anno.anno.annotation.clazz;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * Anno 左树
 * 用于标注左树的注解
 *
 * @author sorghum
 * @since 2023/05/21
 */
@Data
public class AnnoLeftTreeImpl implements AnnoLeftTree {
    /**
     * 左树名称
     */
    private String leftTreeName = "左树";

    /**
     * 分类关键词
     * 查询的时候会用到
     */
    private String catKey = "";

    /**
     * 树类
     */
    private Class<?> treeClass = Object.class;

    /**
     * 是否启用
     */
    private boolean enable = true;

    @Override
    public String leftTreeName() {
        return this.leftTreeName;
    }

    @Override
    public String catKey() {
        return this.catKey;
    }

    @Override
    public Class<?> treeClass() {
        return this.treeClass;
    }

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoLeftTree.class;
    }
}