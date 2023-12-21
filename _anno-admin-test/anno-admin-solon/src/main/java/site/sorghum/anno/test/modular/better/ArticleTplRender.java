package site.sorghum.anno.test.modular.better;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.tpl.TplRender;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文章tpl呈现
 *
 * @author Sorghum
 * @since 2023/12/21
 */
@Slf4j
@Component
public class ArticleTplRender extends TplRender {
    /**
     * 初始化
     */
    public ArticleTplRender() {

        String view = "helloWord.ftl";

        Map<String, Object> props = new HashMap<>();
        Map<String, Object> mp = new LinkedHashMap<>();
        mp.put("annotation", 'E');
        mp.put("core", 'R');
        mp.put("auth", 'U');
        mp.put("web", 'P');
        mp.put("mongodb", 'T');
        mp.put("bi", '-');
        mp.put("job", '-');
        mp.put("tpl", '-');
        mp.put("generator", '-');
        props.put("color", new String[]{
            "#eb776e", "#56aad6", "#69d5e7", "#f686e5", "#29ae94", "#fbd364",
            "#4da1ff", "#ff6e4b", "#ffc524", "#e07de9", "#42e9e1", "#a9f", "#a90",
            "#09f", "#928bff"
        });
        props.put("map", mp);


        init(view,props);
    }

}
