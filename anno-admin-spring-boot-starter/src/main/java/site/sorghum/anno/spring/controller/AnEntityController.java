package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.anno.controller.AnEntityBaseController;

import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@RestController
@RequestMapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AnEntityController extends AnEntityBaseController {

    @Override
    @RequestMapping(value = "/anEntity/{clazz}")
    @SaIgnore
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<Map<Object,Object>> anEntity(@PathVariable String clazz){
        return super.anEntity(clazz);
    }

}
