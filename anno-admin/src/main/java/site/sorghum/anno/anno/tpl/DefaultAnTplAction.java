package site.sorghum.anno.anno.tpl;

import org.noear.solon.annotation.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
@org.springframework.stereotype.Component
public class DefaultAnTplAction {

    /**
     * 模板数据
     */
    public Map<String, Object> data(Map<String,Object> props) {
        return props;
    }



}
