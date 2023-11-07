package site.sorghum.anno.plugin.interfaces;


import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.plugin.service.AuthService;
import site.sorghum.anno.plugin.ao.AnUser;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AuthFunctions {

    public static Function<Map<String, String>, AnUser> verifyLogin = (map) -> {
        String username = map.get("username");
        String password = map.get("password");
        return AnnoBeanUtils.getBean(AuthService.class).verifyLogin(username, password);
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
