package site.sorghum.anno.anno.tpl;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Named
@Slf4j
public class DefaultAnTplAction {

    /**
     * 模板数据
     */
    public Map<String, Object> data(Map<String,Object> props) {
        return props;
    }



}
