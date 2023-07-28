package site.sorghum.amis.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * AmisBase的包装
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Data
public class AmisBaseWrapper {
    /**
     * amis组件
     */
    AmisBase amisBase;

    /*
     * 公共环境变量
     */
    Map<String, Object> commonEnv = new HashMap<>();

    public static AmisBaseWrapper of(AmisBase amisBase) {
        AmisBaseWrapper amisBaseWrapper = new AmisBaseWrapper();
        amisBaseWrapper.setAmisBase(amisBase);
        return amisBaseWrapper;
    }

    public static AmisBaseWrapper of() {
        return of(new AmisBase());
    }
}
