package site.sorghum.anno.test.modular.better;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.tpl.BaseTplRender;
import site.sorghum.anno.auth.AnnoStpUtil;

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
public class ArticleTplRender extends BaseTplRender {

    @Override
    public String getViewName() {
        return "helloWord.ftl";
    }

    @Override
    public void hook() {
        Map<String, Object> existProps = getProps();
        log.info("已经存在的参数：{}", existProps);
        String token = getToken();
        if (token!= null) {
            Object loginIdByToken = AnnoStpUtil.getLoginIdByToken(token);
            log.info("token:{},loginId:{}", token, loginIdByToken);
        }
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
        existProps.put("color", new String[]{
            "#eb776e", "#56aad6", "#69d5e7", "#f686e5", "#29ae94", "#fbd364",
            "#4da1ff", "#ff6e4b", "#ffc524", "#e07de9", "#42e9e1", "#a9f", "#a90",
            "#09f", "#928bff"
        });
        existProps.put("map", mp);
        addProps(existProps);
    }
}
