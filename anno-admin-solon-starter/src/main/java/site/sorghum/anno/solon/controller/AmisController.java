package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.amis.controller.AmisBaseController;

import java.util.HashMap;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@Mapping(value = "/system/config")
public class AmisController extends AmisBaseController {

    @Mapping(value = "/amisJson/{clazz}")
    @SaIgnore
    public AnnoResult<Object> amisJson(String clazz, Context context, @Param("isM2m") boolean isM2m) {
        HashMap<String, Object> data = new HashMap<>(context.paramMap());
        return super.toJson(clazz, data, isM2m);
    }

}
