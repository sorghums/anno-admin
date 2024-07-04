package site.sorghum.anno.anno.annotation.clazz;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * Anno 排序配置
 * <p>
 * 可配置默认排序
 *
 * @author Sorghum
 * @since 2024/07/04
 */
@Data
public class AnnoOrderImpl implements AnnoOrder {
    /**
     * 排序类型 0 asc,1 desc
     */
    private String orderType = "asc";

    /**
     * 排序值，默认无
     */
    private String orderValue = "";

    @Override
    public String orderType() {
        return orderType;
    }

    @Override
    public String orderValue() {
        return orderValue;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoOrder.class;
    }
}