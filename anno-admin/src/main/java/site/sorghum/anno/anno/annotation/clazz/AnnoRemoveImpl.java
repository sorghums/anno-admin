package site.sorghum.anno.anno.annotation.clazz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.*;

/**
 * Anno 删除配置
 * <p>
 * 可配置逻辑删除
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoRemoveImpl implements AnnoRemove {
    /**
     * 删除类型 0 物理删除 1 逻辑删除
     */
    int removeType = DEFAULT_REMOVE_TYPE;

    /**
     * 逻辑删除值
     * 默认 1
     */
    String removeValue = DEFAULT_REMOVE_VALUE;

    /**
     * 逻辑删除值
     * 默认 0
     */
    String notRemoveValue = DEFAULT_NOT_REMOVE_VALUE;

    /**
     * 逻辑删除字段
     * 默认 del_flag
     */
    String removeField = DEFAULT_REMOVE_FIELD;

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoRemove.class;
    }

    @Override
    public int removeType() {
        return this.removeType;
    }

    @Override
    public String removeValue() {
        return this.removeValue;
    }

    @Override
    public String notRemoveValue() {
        return this.notRemoveValue;
    }

    @Override
    public String removeField() {
        return this.removeField;
    }
}
