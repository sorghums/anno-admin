package site.sorghum.anno.anno.annotation.field.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * Anno 图像类型
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoImageTypeImpl implements AnnoImageType {
    /**
     * 点击可放大展示
     */
    private boolean enlargeAble = true;

    /**
     * 宽度 px
     */
    private int width = 50;

    /**
     * 高度 px
     */
    private int height = 50;

    @Override
    public boolean enlargeAble() {
        return this.enlargeAble;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoImageType.class;
    }
}