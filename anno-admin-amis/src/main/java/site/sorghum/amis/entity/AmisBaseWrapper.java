package site.sorghum.amis.entity;

import lombok.Data;

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

    public static AmisBaseWrapper of(AmisBase amisBase) {
        AmisBaseWrapper amisBaseWrapper = new AmisBaseWrapper();
        amisBaseWrapper.setAmisBase(amisBase);
        return amisBaseWrapper;
    }

    public static AmisBaseWrapper of() {
        return of(new AmisBase());
    }
}
