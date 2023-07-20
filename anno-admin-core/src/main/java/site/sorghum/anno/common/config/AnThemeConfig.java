package site.sorghum.anno.common.config;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.view.thymeleaf.ThymeleafRender;

/**
 * An主题配置
 *
 * @author Sorghum
 * @since 2023/07/20
 */
@Component
public class AnThemeConfig {

    /**
     * 初始化主题配置
     */
    @Init
    public void init(){
        String theme = Solon.cfg().get("anno-admin.theme", "antd");
        ThymeleafRender.global().putVariable("anno_theme",theme);
    }
}
