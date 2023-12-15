package tech.powerjob.server.solon.core.alarm.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import tech.powerjob.common.OmsConstant;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.utils.NetUtils;
import tech.powerjob.server.solon.common.PowerJobServerConfigKey;
import tech.powerjob.server.solon.common.SJ;
import tech.powerjob.server.solon.extension.alarm.Alarm;
import tech.powerjob.server.solon.extension.alarm.AlarmTarget;
import tech.powerjob.server.solon.extension.alarm.Alarmable;

import java.util.List;
import java.util.Set;

/**
 * 钉钉告警服务
 *
 * @author tjq
 * @since 2020/8/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DingTalkAlarmService implements Alarmable, LifecycleBean {

    private Long agentId;
    private DingTalkUtils dingTalkUtils;
    private Cache<String, String> mobile2UserIdCache;

    private static final int CACHE_SIZE = 8192;
    /**
     * 防止缓存击穿
     */
    private static final String EMPTY_TAG = "EMPTY";

    @Override
    public void onFailed(Alarm alarm, List<AlarmTarget> targetUserList) {
        if (dingTalkUtils == null) {
            return;
        }
        Set<String> userIds = Sets.newHashSet();
        targetUserList.forEach(user -> {
            String phone = user.getPhone();
            if (StringUtils.isEmpty(phone)) {
                return;
            }
            try {
                String userId = mobile2UserIdCache.get(phone, () -> {
                    try {
                        return dingTalkUtils.fetchUserIdByMobile(phone);
                    } catch (PowerJobException ignore) {
                        return EMPTY_TAG;
                    } catch (Exception ignore) {
                        return null;
                    }
                });
                if (!EMPTY_TAG.equals(userId)) {
                    userIds.add(userId);
                }
            } catch (Exception ignore) {
            }
        });
        userIds.remove(null);

        if (!userIds.isEmpty()) {
            String userListStr = SJ.COMMA_JOINER.skipNulls().join(userIds);
            List<DingTalkUtils.MarkdownEntity> markdownEntities = Lists.newLinkedList();
            markdownEntities.add(new DingTalkUtils.MarkdownEntity("server", NetUtils.getLocalHost()));
            String content = alarm.fetchContent().replaceAll(OmsConstant.LINE_SEPARATOR, OmsConstant.COMMA);
            markdownEntities.add(new DingTalkUtils.MarkdownEntity("content", content));

            try {
                dingTalkUtils.sendMarkdownAsync(alarm.fetchTitle(), markdownEntities, userListStr, agentId);
            } catch (Exception e) {
                log.error("[DingTalkAlarmService] send ding message failed, reason is {}", e.getMessage());
            }
        }
    }

    @Override
    public void start() throws Throwable {
        String agentId = Solon.cfg().getProperty(PowerJobServerConfigKey.DING_AGENT_ID);
        String appKey = Solon.cfg().getProperty(PowerJobServerConfigKey.DING_APP_KEY);
        String appSecret = Solon.cfg().getProperty(PowerJobServerConfigKey.DING_APP_SECRET);

        log.info("[DingTalkAlarmService] init with appKey:{},appSecret:{},agentId:{}", appKey, appSecret, agentId);

        if (StringUtils.isAnyBlank(agentId, appKey, appSecret)) {
            log.warn("[DingTalkAlarmService] cannot get agentId, appKey, appSecret at the same time, this service is unavailable");
            return;
        }
        if (!StringUtils.isNumeric(agentId)) {
            log.warn("[DingTalkAlarmService] DingTalkAlarmService is unavailable due to invalid agentId: {}", agentId);
            return;
        }
        this.agentId = Long.valueOf(agentId);
        dingTalkUtils = new DingTalkUtils(appKey, appSecret);
        mobile2UserIdCache = CacheBuilder.newBuilder().maximumSize(CACHE_SIZE).softValues().build();
        log.info("[DingTalkAlarmService] init DingTalkAlarmService successfully!");
    }

}
