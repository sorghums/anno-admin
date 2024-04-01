package site.sorghum.anno.plugin.tpl;

import cn.hutool.core.map.MapUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.anno.tpl.BaseTplRender;
import site.sorghum.anno.auth.AnnoStpUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * anno-sql细节tpl呈现
 *
 * @author Sorghum
 * @since 2024/01/29
 */
@Named
@Slf4j
public class AnnoSqlDetailTplRender extends BaseTplRender {
    @Override
    public String getViewName() {
        return "AnnoSqlDetailTplRender.ftl";
    }

    @Override
    public void hook() {
        Map<String, Object> existProps = getProps();
        log.info("已经存在的参数：{}", existProps);
        String token = getToken();
        if (token != null) {
            Object loginIdByToken = AnnoStpUtil.getLoginIdByToken(token);
            if (loginIdByToken == null) {
                return;
            }
        }
        String sqlContent = MapUtil.getStr(existProps, "sqlContent");
        Map<String, Object> mp = new LinkedHashMap<>();
        mp.put("sqlContent", sqlContent);
        addProps(mp);
    }
}
