package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping(value = "/system/config")
public class AmisController extends AmisBaseController {

    @RequestMapping(value = "/amisJson/{clazz}")
    @SaIgnore
    public AnnoResult<Object> amisJson(@PathVariable String clazz, @RequestParam("isM2m") boolean isM2m,@RequestBody HashMap<String, Object> paramMap) {
        return super.toJson(clazz, paramMap, isM2m);
    }

}