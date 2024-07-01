package site.sorghum.anno.plugin.javacmd;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.request.UpdatePwdReq;
import site.sorghum.anno.plugin.service.AuthService;

@Slf4j
@Named
public class ResetPwdJavaCmdSupplier implements JavaCmdSupplier {

    @Inject
    AuthService authService;

    @Override
    public String run(CommonParam param) {
        AnUser anUser = param.toT(AnUser.class);
        CommonParam extraInput = param.getExtraInput();
        UpdatePwdReq updatePwdReq = new UpdatePwdReq();
        updatePwdReq.setUserId(anUser.getId());
        updatePwdReq.setNewPwd1(extraInput.getString("newPwd1"));
        updatePwdReq.setNewPwd2(extraInput.getString("newPwd2"));
        updatePwdReq.setFromAdmin(true);
        authService.updatePwd(updatePwdReq);
        return "js://createMessage.success('密码重置成功')";
    }
}
