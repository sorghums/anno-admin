package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.amis.controller.AmisBaseController;

import java.util.HashMap;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@Api(tags = "功能控制器")
@Mapping(value = AnnoConstants.BASE_URL + "/system/config")
public class AmisController extends AmisBaseController {

    @Override
    @Mapping(value = "/anEntity/{clazz}")
    @SaIgnore
    @ApiOperation(value = "获取实体信息", notes = "获取实体信息")
    public AnnoResult<AnEntity> anEntity(@Path String clazz){
        return AnnoResult.succeed(metadataManager.getEntity(clazz));
    }

}
