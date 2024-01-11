package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.anno.controller.AnEntityBaseController;

import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@Api(tags = "功能控制器")
@Mapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AnEntityController extends AnEntityBaseController {

    @Override
    @Mapping(value = "/anEntity/{clazz}")
    @SaIgnore
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<Map<Object,Object>> anEntity(@Path String clazz){
        return super.anEntity(clazz);
    }

}
