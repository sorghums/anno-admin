package site.sorghum.anno.db;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import site.sorghum.anno.anno.interfaces.PrimaryKeyModelInterfaces;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/8/18 19:01
 */
@Data
public class PrimaryKeyModel implements Serializable, PrimaryKeyModelInterfaces {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    protected String id;

    @JoinResMap
    @Column(ignore = true)
    Map<String, Object> joinResMap = new HashMap<>();
}
