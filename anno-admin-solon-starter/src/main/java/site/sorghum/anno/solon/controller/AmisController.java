package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.json.JSONObject;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.controller.AmisBaseController;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@Mapping(value = "/system/config")
public class AmisController extends AmisBaseController {
    @Inject
    MetadataManager metadataManager;

    @Mapping(value = "/amisJson/{clazz}")
    @SaIgnore
    public AnnoResult<Object> amisJ3son(String clazz, Context context, @Param("isM2m") boolean isM2m) {
        HashMap<String, Object> data = new HashMap<>(context.paramMap());
        return super.toJson(clazz, data, isM2m);
    }

    @Mapping(value = "/anEntity/{clazz}")
    @SaIgnore
    public AnnoResult<AnEntity> anEntity(@Path String clazz){
        return AnnoResult.succeed(metadataManager.getEntity(clazz));
    }

}
