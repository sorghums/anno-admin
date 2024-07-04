package site.sorghum.anno.anno.annotation.clazz;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * AnnoMain注解
 * <p>
 * 用于标注主要的模板类
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
public class AnnoPermissionImpl implements AnnoPermission {
    /**
     * 启用
     */
    private boolean enable = true;

    /**
     * 基础权限名称(即查看权限)
     * 如：sys_org
     */
    private String baseCode = "";

    /**
     * 基础代码翻译
     */
    private String baseCodeTranslate = "";

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public String baseCode() {
        return this.baseCode;
    }

    @Override
    public String baseCodeTranslate() {
        return this.baseCodeTranslate;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoPermission.class;
    }
}