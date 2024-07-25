package site.sorghum.p4j;

import org.pf4j.ExtensionPoint;

public interface AnnoAdminPf4j extends ExtensionPoint {
    /**
     * 加载实体
     */
    void loadEntity();
}
