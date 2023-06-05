package site.sorghum.anno.modular.message.controller;

import site.sorghum.anno.modular.message.entity.response.AnnoMessageResponse;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.util.List;

/**
 * 菜单控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Mapping(value = "/system/config")
@Controller
public class MessageController {

    @Mapping(value = "/dataMessage")
    public List<AnnoMessageResponse> dataMenu() {
        return AnnoMessageResponse.DEMO();
    }
}
