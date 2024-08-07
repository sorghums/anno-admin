package site.sorghum.anno.anno.annotation.field.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * 代码类型
 *
 * @author Sorghum
 * @since 2024/07/08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoCodeTypeImpl implements AnnoCodeType {

    /**
     * 返回一个表示模式的字符串，默认为 "text/x-yaml"。
     *
     *
     */
    String mode =  MODE;

    /**
     * 获取主题名称的注解元素（可选）
     *
     */
    String theme = THEME;

    @Override
    public String mode() {
        return this.mode;
    }

    @Override
    public String theme() {
        return this.theme;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoCodeType.class;
    }
}
