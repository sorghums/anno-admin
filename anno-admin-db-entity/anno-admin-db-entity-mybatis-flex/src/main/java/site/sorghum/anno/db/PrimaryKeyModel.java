package site.sorghum.anno.db;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
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
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    protected String id;

    @JoinResMap
    @Column(ignore = true)
    Map<String, Object> joinResMap = new HashMap<>();
}
