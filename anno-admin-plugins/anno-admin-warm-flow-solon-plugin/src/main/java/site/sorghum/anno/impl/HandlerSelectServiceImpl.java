package site.sorghum.anno.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.dromara.warm.flow.ui.dto.HandlerFunDto;
import org.dromara.warm.flow.ui.dto.HandlerQuery;
import org.dromara.warm.flow.ui.service.HandlerSelectService;
import org.dromara.warm.flow.ui.vo.HandlerSelectVo;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.plugin.ao.AnOrg;
import site.sorghum.anno.plugin.ao.AnRole;
import site.sorghum.anno.plugin.dao.AnOrgDao;
import site.sorghum.anno.plugin.dao.AnRoleDao;

import java.util.List;

@Component
public class HandlerSelectServiceImpl implements HandlerSelectService {
    @Inject
    AnRoleDao anRoleDao;
    @Inject
    AnOrgDao anOrgDao;

    @Override
    public List<String> getHandlerType() {
        return List.of(
            "角色",
            "组织"
        );
    }

    @Override
    public HandlerSelectVo getHandlerSelect(HandlerQuery handlerQuery) {
        String handlerType = handlerQuery.getHandlerType();
        if ("角色".equals(handlerType)) {
            return getRole(handlerQuery);
        }
        if ("组织".equals(handlerType)) {
            return getOrg(handlerQuery);
        }
        return new HandlerSelectVo();
    }

    /**
     * 获取角色列表
     *
     * @param query 查询条件
     * @return HandlerSelectVo
     */
    private HandlerSelectVo getRole(HandlerQuery query) {
        // 查询角色列表
        List<AnRole> roleList = anRoleDao.list();
        long total = roleList.size();

        // 业务系统数据，转成组件内部能够显示的数据, total是业务数据总数，用于分页显示
        HandlerFunDto<AnRole> handlerFunDto = new HandlerFunDto<>(roleList, total)
            // 以下设置获取内置变量的Function
            .setStorageId(role -> "role:" + role.getId()) // 前面拼接role:  是为了防止用户、角色的主键重复
            .setHandlerCode(AnRole::getRoleCode) // 权限编码
            .setHandlerName(AnRole::getRoleName) // 权限名称
            .setGroupName((it) -> "默认分组")
            .setCreateTime(role -> LocalDateTimeUtil.formatNormal(role.getCreateTime()));
        return getHandlerSelectVo(handlerFunDto);
    }

    private HandlerSelectVo getOrg(HandlerQuery query) {
        // 查询权限列表
        List<AnOrg> anOrgList = anOrgDao.list();
        long total = anOrgList.size();

        // 业务系统数据，转成组件内部能够显示的数据, total是业务数据总数，用于分页显示
        HandlerFunDto<AnOrg> handlerFunDto = new HandlerFunDto<>(anOrgList, total)
            // 以下设置获取内置变量的Function
            .setStorageId(it -> "org:" + it.getId()) // 前面拼接role:  是为了防止用户、角色的主键重复
            .setHandlerCode(AnOrg::getId) // 权限编码
            .setHandlerName(AnOrg::getOrgName) //
            .setGroupName((it) -> "默认分组")
            .setCreateTime(role -> LocalDateTimeUtil.formatNormal(role.getCreateTime()));
        return getHandlerSelectVo(handlerFunDto);
    }

}
