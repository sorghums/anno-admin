package site.sorghum.anno._metadata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.annotation.field.AnnoEditImpl;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoFieldImpl;
import site.sorghum.anno.anno.annotation.field.AnnoSearchImpl;

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
     * jsonPath路径
     */
    private String jsonPath;
    /**
     * 列类型
     */
    private Class<?> javaType;
    /**
     * java类醒
     */
    private Field javaField;


    /**
     * 简单构造器
     * @param fieldName         字段名
     * @param tableFieldName    数据库列名
     * @param title             标题
     * @param addEnable         是否允许新增时编辑此字段
     * @param editEnable        是否允许编辑时编辑此字段
     * @return  AnField
     */
    public static AnField simpleNew(String fieldName,
                                    String tableFieldName,
                                    String title,
                                    boolean addEnable,
                                    boolean editEnable) {
        AnField anField = new AnField();
        anField.setJavaName(fieldName);
        anField.setTitle(title);
        anField.setTableFieldName(tableFieldName);
        anField.setSearch(AnnoSearchImpl.builder().build());
        anField.setEdit(AnnoEditImpl
            .builder()
            .addEnable(addEnable)
            .editEnable(editEnable)
            .showBy(AnnoEditImpl
                .ShowByImpl
                .builder()
                .enable(false)
                .build()
            )
            .build());
        return anField;
    }
}
