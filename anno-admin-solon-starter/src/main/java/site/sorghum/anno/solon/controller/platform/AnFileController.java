package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.plugin.controller.AnBaseFileController;

import java.io.InputStream;

/**
 * 一个文件控制器
 *
 * @author Sorghum
 * @since 2023/08/02
 */
@Mapping(value = AnnoConstants.BASE_URL + "/anLocal")
@Controller
@Condition(onClass = AnnoPlatform.class)
@SaIgnore
public class AnFileController{
    @Inject
    AnBaseFileController anBaseFileController;

    @Inject
    AnnoProperty annoProperty;
    @Get
    @Mapping(value = "/**")
    @SaIgnore
    public void getFile(Context context) {
        String path = context.path();
        // 获取anLocal后面的路径
        path = path.substring(path.indexOf("anLocal") + 7);
        InputStream inputStream
                = anBaseFileController.getFile(path);
        context.output(inputStream);
    }


}
