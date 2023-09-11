package site.sorghum.anno.test.modular.better;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.tpl.DefaultAnTplAction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class ArticleTplAction extends DefaultAnTplAction {
    @Override
    public Map<String, Object> data(Map<String, Object> props) {
        log.info("获取到的前置参数：" + props);
        Map<String, Object> map = new HashMap<>();
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
        map.put("color", new String[]{
            "#eb776e", "#56aad6", "#69d5e7", "#f686e5", "#29ae94", "#fbd364",
            "#4da1ff", "#ff6e4b", "#ffc524", "#e07de9", "#42e9e1", "#a9f", "#a90",
            "#09f", "#928bff"
        });
        map.put("map", mp);
        return map;
    }
}
