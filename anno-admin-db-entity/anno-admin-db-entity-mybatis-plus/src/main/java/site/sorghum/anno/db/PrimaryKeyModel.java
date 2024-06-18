package site.sorghum.anno.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/8/18 19:01
 */
@Data
public class PrimaryKeyModel implements Serializable {

    @AnnoField(title = "主键", tableFieldName = "id", show = false, fieldSize = 32, insertWhenNullSet = SnowIdSupplier.class)
    @PrimaryKey
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected String id;

    @JoinResMap
    @TableField(exist = false)
    Map<String, Object> joinResMap = new HashMap<>();
}
