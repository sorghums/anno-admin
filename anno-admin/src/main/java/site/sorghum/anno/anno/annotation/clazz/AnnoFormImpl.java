package site.sorghum.anno.anno.annotation.clazz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * AnnoForm注解实现类
 * 用于标注主要的Form模板类
 *
 * @author sorghum
 * @since 2024/06/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoFormImpl implements AnnoForm {
    /**
     * 名称
     */
    private String name;

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoForm.class;
    }
}