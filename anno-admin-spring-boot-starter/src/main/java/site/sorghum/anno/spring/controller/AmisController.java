package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.web.bind.annotation.*;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.amis.controller.AmisBaseController;

import java.util.HashMap;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@RestController
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AmisController extends AmisBaseController {

    @RequestMapping(value = "/amisJson/{clazz}")
    @SaIgnore
    public AnnoResult<Object> amisJson(@PathVariable String clazz, @RequestParam(value = "isM2m",defaultValue = "false") boolean isM2m,@RequestBody(required = false) HashMap paramMap) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        return super.toJson(clazz, paramMap, isM2m);
    }

}
