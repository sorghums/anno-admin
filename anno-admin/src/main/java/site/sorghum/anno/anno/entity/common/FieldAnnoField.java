package site.sorghum.anno.anno.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno.anno.annotation.field.AnnoField;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
public class FieldAnnoField {
    /**
     * 字段
     */
    Field field;
    /**
     * 字段注解
     */
    AnnoField annoField;
    /**
     * 主键注解
     */
    PrimaryKey primaryKey;
}
