package site.sorghum.anno.plugin.interfaces;


import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.request.LoginReq;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AuthFunctions {

    public static Function<LoginReq, AnUser> verifyLogin = (req) -> {
        return AnnoBeanUtils.getBean(AuthService.class).verifyLogin(req.getUsername(), req.getPassword());
    };

    public static Function<String, AnUser> getUserById = (id) -> {
        return AnnoBeanUtils.getBean(AuthService.class).getUserById(id);
    };

    public static Function<String, List<String>> permissionList = (userId) -> {
        return AnnoBeanUtils.getBean(AuthService.class).permissionList(userId);
    };

    public static Function<String, List<String>> roleList = (userId) -> {
        return AnnoBeanUtils.getBean(AuthService.class).roleList(userId);
    };

    public static Consumer<String> removePermRoleCacheList = (userId) -> {
        AnnoBeanUtils.getBean(AuthService.class).removePermRoleCacheList(userId);
    };
}
