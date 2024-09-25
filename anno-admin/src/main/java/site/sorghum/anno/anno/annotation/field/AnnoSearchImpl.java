package site.sorghum.anno.anno.annotation.field;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
import site.sorghum.anno.db.QueryType;

import java.lang.annotation.Annotation;

/**
 * Anno搜索
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoSearchImpl implements AnnoSearch {
    /**
     * 是否必填
     */
    boolean notNull = false;

    /**
     * 启用
     */
    boolean enable = true;

    /**
     * 查询类型
     */
    QueryType queryType = QueryType.EQ;

    /**
     * 默认值
     */
    Object defaultValue = "";


    /**
     * 默认值供应商
     */
    Class<? extends FieldBaseSupplier> defaultValueSupplier = FieldBaseSupplier.class;

    /**
     * 提示信息
     */
    String placeHolder = "";

    @Override
    public boolean notNull() {
        return this.notNull;
    }

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public QueryType queryType() {
        return this.queryType;
    }

    @Override
    public String placeHolder() {
        return this.placeHolder;
    }

    @Override
    public String defaultValue() {
        return Convert.toStr(this.defaultValue);
    }

    @Override
    public Class<? extends FieldBaseSupplier> defaultValueSupplier() {
        return this.defaultValueSupplier;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoSearch.class;
    }
}
