package site.sorghum.anno.pre.plugin.proxy;

import jakarta.inject.Named;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.pre.plugin.ao.SysRole;

import java.util.List;

@Named
public class SysRoleProxy implements AnnoBaseProxy<SysRole> {

    @Override
    public void beforeFetch(TableParam<SysRole> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @Override
    public void beforeAdd(TableParam<SysRole> tableParam, SysRole data) {

    }

    @Override
    public void afterAdd(SysRole data) {

    }

    @Override
    public void beforeUpdate(TableParam<SysRole> tableParam, List<DbCondition> dbConditions, SysRole data) {

    }

    @Override
    public void afterUpdate(SysRole data) {

    }

    @Override
    public void beforeDelete(TableParam<SysRole> tableParam, List<DbCondition> dbConditions) {
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

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }

    @Override
    public void afterFetch(TableParam<SysRole> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<SysRole> page) {

    }
}
