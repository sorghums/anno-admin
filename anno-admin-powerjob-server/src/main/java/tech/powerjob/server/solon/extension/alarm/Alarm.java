package tech.powerjob.server.solon.extension.alarm;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import tech.powerjob.common.OmsConstant;
import tech.powerjob.common.PowerSerializable;
import tech.powerjob.common.utils.CommonUtils;

/**
 * 报警内容
 *
 * @author tjq
 * @since 2020/8/1
 */
public interface Alarm extends PowerSerializable {

    String fetchTitle();

    default String fetchContent() {
        StringBuilder sb = new StringBuilder();
        JSONObject content = JSONObject.parseObject(JSONObject.toJSONString(this));
        content.forEach((key, originWord) -> {
            sb.append(key).append(": ");
            String word = String.valueOf(originWord);
            if (StringUtils.endsWithIgnoreCase(key, "time") || StringUtils.endsWithIgnoreCase(key, "date")) {
                try {
                    if (originWord instanceof Long) {
                        word = CommonUtils.formatTime((Long) originWord);
                    }
                }catch (Exception ignore) {
                }
            }
            sb.append(word).append(OmsConstant.LINE_SEPARATOR);
        });
        return sb.toString();
    }
}
