package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.plugin.ao.AnRole;
import site.sorghum.anno.plugin.ao.AnRolePermission;
import site.sorghum.anno.plugin.ao.AnUserRole;
import site.sorghum.anno.plugin.function.AuthFunction;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色代理
 *
 * @author Sorghum
 * @since 2023/09/08
 */
@Named
@Slf4j
public class AnRoleProxy implements AnnoBaseProxy<AnRole> {

    @Inject
    @Named("dbServiceWood")
    DbService dbService;

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

    @Override
    public void afterDelete(DbCriteria criteria) {
        List<String> roleIds = getDeleteRoleIds(criteria);
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        Object[] roleIdArr = roleIds.toArray();
        // 关联数据删除前，先把受影响的用户取出来
        Set<String> userIds = new LinkedHashSet<>();
        List<AnUserRole> userRoles = dbService.list(
            DbCriteria.fromClass(AnUserRole.class).in("role_id", roleIdArr));
        for (AnUserRole userRole : userRoles) {
            if (StrUtil.isNotBlank(userRole.getUserId())) {
                userIds.add(userRole.getUserId());
            }
        }
        // 清理指向已删除角色的关联数据，否则用户仍会命中该角色的权限
        dbService.delete(DbCriteria.fromClass(AnUserRole.class).in("role_id", roleIdArr));
        dbService.delete(DbCriteria.fromClass(AnRolePermission.class).in("role_id", roleIdArr));
        // 权限缓存默认2小时，需主动失效
        for (String userId : userIds) {
            try {
                AuthFunction.removePermRoleCacheList.accept(userId);
            } catch (Exception e) {
                log.error("清除用户[{}]角色权限缓存失败", userId, e);
            }
        }
    }

    /**
     * 从删除条件中取出被删除的角色id
     *
     * @param criteria 删除条件
     * @return {@link List}<{@link String}>
     */
    private List<String> getDeleteRoleIds(DbCriteria criteria) {
        List<String> roleIds = new ArrayList<>();
        List<DbCondition> conditions = criteria.findCondition("id");
        if (CollUtil.isEmpty(conditions)) {
            return roleIds;
        }
        for (DbCondition condition : conditions) {
            for (Object value : condition.getValues()) {
                String roleId = StrUtil.toString(value);
                if (StrUtil.isNotBlank(roleId) && !roleIds.contains(roleId)) {
                    roleIds.add(roleId);
                }
            }
        }
        return roleIds;
    }

}
