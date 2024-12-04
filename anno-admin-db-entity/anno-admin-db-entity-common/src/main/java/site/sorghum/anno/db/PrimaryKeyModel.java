package site.sorghum.anno.db;

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

    protected String id;

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
