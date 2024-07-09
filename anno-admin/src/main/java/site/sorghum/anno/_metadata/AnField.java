package site.sorghum.anno._metadata;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoFieldImpl;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoFileType;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
import site.sorghum.anno.anno.tree.TreeDataSupplier;
import site.sorghum.anno.db.QueryType;

import java.lang.reflect.Field;
import java.util.List;

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
