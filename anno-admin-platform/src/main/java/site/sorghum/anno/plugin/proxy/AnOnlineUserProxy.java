package site.sorghum.anno.plugin.proxy;

import cn.dev33.satoken.session.SaSession;
import jakarta.inject.Named;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.plugin.ao.AnOnlineUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Named
public class AnOnlineUserProxy implements AnnoBaseProxy<AnOnlineUser> {

    @Override
    public String[] supportEntities() {
        return new String[]{
                AnnoBaseProxy.clazzToDamiEntityName(AnOnlineUser.class)
        };
    }

    @Override
    public void afterFetch(Class<AnOnlineUser> anOnlineUserClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<AnOnlineUser> page) {
        List<String> sessionStr = AnnoStpUtil.searchSessionId("", pageParam.getOffset(), pageParam.getPageSize(), false);
        Set<String> sessions = sessionStr.stream().map(s -> s.split(":")[s.split(":").length-1]).collect(Collectors.toSet());

        List<AnOnlineUser> userList = new ArrayList<>();
        for (String s : sessions) {
            SaSession session = AnnoStpUtil.getSessionByLoginId(s);
            AnnoAuthUser authUser = (AnnoAuthUser) session.get("authUser");
            userList.add(AnOnlineUser.authToOnlineUser(authUser));
        }
        page.getList().addAll(userList);
    }
}
