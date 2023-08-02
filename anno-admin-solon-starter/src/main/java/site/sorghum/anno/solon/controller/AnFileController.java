package site.sorghum.anno.solon.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno.pre.plugin.controller.AnBaseFileController;

import java.io.InputStream;

/**
 * 一个文件控制器
 *
 * @author Sorghum
 * @since 2023/08/02
 */
@Mapping(value = "anLocal")
@Controller
public class AnFileController extends AnBaseFileController {

    @Get
    @Mapping(value = "/**")
    public void getFile(Context context) {
        String path = context.path();
        // 获取anLocal后面的路径
        path = path.substring(path.indexOf("anLocal") + 7);
        InputStream inputStream
                = super.getFile(path);
        context.output(inputStream);
    }


}
