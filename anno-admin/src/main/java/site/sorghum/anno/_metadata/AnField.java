package site.sorghum.anno._metadata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoFieldImpl;

import java.lang.reflect.Field;

/**
 * @author songyinyin
 * @see AnnoField
 * @since 2023/7/9 21:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AnField extends AnnoFieldImpl {
    /**
     * 列名
     */
    private String javaName;
    /**
     * 列类型
     */
    private Class<?> javaType;
    /**
     * java类醒
     */
    private Field javaField;
}
