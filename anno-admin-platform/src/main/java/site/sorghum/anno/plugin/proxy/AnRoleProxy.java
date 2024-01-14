package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.plugin.ao.AnRole;

import java.util.List;

@Named
public class AnRoleProxy implements AnnoBaseProxy<AnRole> {

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(AnRole.class)
        };
    }

    @Override
    public void beforeDelete(DbCriteria criteria) {
        List<DbCondition> conditions = criteria.findCondition("id");
        if (CollUtil.isNotEmpty(conditions)) {
            for (DbCondition condition : conditions) {
                if (condition.getValues().contains("admin")) {
                    throw new BizException("管理员角色不可删除");
                }
            }
        }
    }

}
