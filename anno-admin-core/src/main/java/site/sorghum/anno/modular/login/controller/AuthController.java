package site.sorghum.anno.modular.login.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.MethodType;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.menu.entity.model.AnnoMenu;
import site.sorghum.anno.modular.menu.entity.response.AnnoMenuResponse;
import site.sorghum.anno.modular.system.anno.SysUser;
import site.sorghum.anno.response.AnnoResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Auth控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = "/system/auth")
@Controller
public class AuthController {

    @Inject
    AnnoService annoService;

    @Mapping(value = "/login", method = MethodType.POST,consumes = "application/json")
    public AnnoResult<String> login(@Body Map<String ,String> user) {
        String mobile = user.get("mobile");
        String password = user.get("password");
        String codeKey = user.get("codeKey");
        String code = user.get("code");
        if (mobile == null || password == null) {
            return AnnoResult.failure("用户名或密码不能为空");
        }
        StpUtil.login(mobile);
        return AnnoResult.succeed(StpUtil.getTokenValue());
    }
}
