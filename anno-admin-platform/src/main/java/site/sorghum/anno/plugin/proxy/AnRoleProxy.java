package site.sorghum.anno.plugin.proxy;

import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.plugin.ao.AnRole;

import java.util.List;

@Named
public class AnRoleProxy implements AnnoBaseProxy<AnRole> {

    @Override
    public void beforeDelete(Class<AnRole> tClass, List<DbCondition> dbConditions) {
        for (DbCondition dbCondition : dbConditions) {
            String field = dbCondition.getField();
            if (field.equals("id")){
                if (dbCondition.getValue().equals("admin")){
                    throw new BizException("管理员角色不可删除");
                }
                return;
            }
        }
    }

}
