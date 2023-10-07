package site.sorghum.anno.suppose.model;

import lombok.Data;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno.anno.annotation.field.AnnoField;

import java.io.Serializable;

/**
 * @author songyinyin
 * @since 2023/8/18 19:01
 */
@Data
public class PrimaryKeyModel implements Serializable {

    @AnnoField(title = "主键", tableFieldName = "id", show = false, fieldSize = 32)
    @PrimaryKey
    protected String id;
}
