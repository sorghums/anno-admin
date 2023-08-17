package site.sorghum.anno._metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AnMany2ManyField {
    /**
     * 字段
     */
    private Field field;
    /**
     * 中间表名称
     */
    private String mediumTable;
    /**
     * [当前表][中间表] 列
     */
    private String thisColumnMediumName;
    /**
     * [当前表][当前表] 列
     */
    private String thisColumnReferencedName;

    /**
     * [他表][中间表] 列
     */
    private String otherColumnMediumName;
    /**
     * [他表][他表] 列
     */
    private String otherColumnReferencedName;

}
