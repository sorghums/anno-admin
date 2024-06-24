package tech.powerjob.server.solon.core.alarm.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import tech.powerjob.common.OmsConstant;
import tech.powerjob.common.utils.CollectionUtils;
import tech.powerjob.common.utils.HttpUtils;
import tech.powerjob.server.solon.extension.alarm.Alarm;
import tech.powerjob.server.solon.extension.alarm.AlarmTarget;
import tech.powerjob.server.solon.extension.alarm.Alarmable;

import java.util.List;

/**
 * http 回调报警
 *
 * @author tjq
 * @since 11/14/20
 */
@Slf4j
@Component
public class WebHookAlarmService implements Alarmable {

    private static final String HTTP_PROTOCOL_PREFIX = "http://";
    private static final String HTTPS_PROTOCOL_PREFIX = "https://";

    @Override
    public void onFailed(Alarm alarm, List<AlarmTarget> targetUserList) {
        if (CollectionUtils.isEmpty(targetUserList)) {
            return;
        }
        targetUserList.forEach(user -> {
            String webHook = user.getWebHook();
            if (StringUtils.isEmpty(webHook)) {
                return;
            }

            // 自动添加协议头
            if (!webHook.startsWith(HTTP_PROTOCOL_PREFIX) && !webHook.startsWith(HTTPS_PROTOCOL_PREFIX)) {
                webHook = HTTP_PROTOCOL_PREFIX + webHook;
            }

            MediaType jsonType = MediaType.parse(OmsConstant.JSON_MEDIA_TYPE);
            RequestBody requestBody = RequestBody.create(jsonType, JSONObject.toJSONString(alarm));

            try {
                String response = HttpUtils.post(webHook, requestBody);
                log.info("[WebHookAlarmService] invoke webhook[url={}] successfully, response is {}", webHook, response);
            } catch (Exception e) {
                log.warn("[WebHookAlarmService] invoke webhook[url={}] failed!", webHook, e);
            }
        });
    }
}
