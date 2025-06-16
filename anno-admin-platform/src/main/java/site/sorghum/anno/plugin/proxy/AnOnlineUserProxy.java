package site.sorghum.anno.plugin.proxy;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoDefaultImpl;
import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.date.DateUtil;
import jakarta.inject.Named;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.plugin.ao.AnOnlineUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
public class AnOnlineUserProxy implements AnnoBaseProxy<AnOnlineUser> {
    /**
     * 后缀
     */
    private String suffix = "*";

    private boolean initSuffix = false;

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<AnOnlineUser> page) {
        initSuffix();
        DbPage dbPage = criteria.getPageOrDefault();
        List<String> tokenValues = AnnoStpUtil.searchTokenValue("", dbPage.getOffset(), dbPage.getPageSize(), false);
        List<String> actualTokens = tokenValues.stream().map(s -> s.split(":")[s.split(":").length - 1]).toList();
        Date now = new Date();
        List<AnOnlineUser> userList = new ArrayList<>();
        for (String actualToken : actualTokens) {
            SaSession session = AnnoStpUtil.getTokenSessionByToken(actualToken);
            AnnoAuthUser authUser = JSONUtil.toBean(session.get("authUser"), AnnoAuthUser.class);
            AnOnlineUser onlineUser = AnOnlineUser.authToOnlineUser(authUser);
            onlineUser.setExpireTime(
                DateUtil.offsetSecond(now, (int) AnnoStpUtil.getTokenTimeout(actualToken))
            );
            onlineUser.setToken(actualToken.substring(0, actualToken.length() - 8) + suffix);
            userList.add(onlineUser);
        }
        page.getList().addAll(userList);
    }

    private void initSuffix() {
        if (initSuffix) {
            return;
        }
        SaTokenDao saTokenDao = SaManager.getSaTokenDao();
        if (saTokenDao instanceof SaTokenDaoDefaultImpl) {
            suffix = "";
        }
        initSuffix = true;
    }
}
