package site.sorghum.anno.plugin.javacmd;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.Map;

@Slf4j
@Named
public class ForceLogOutJavaCmdSupplier implements JavaCmdSupplier {

    @Inject
    AuthService authService;

    @Override
    public String run(Map<String, Object> param) {
        authService.forceLogout(param);
        return "js://createMessage.success('已将用户强制退出')";
    }
}
