package tech.powerjob.server.solon.core.alarm;

import site.sorghum.anno.plugin.ao.AnUser;
import tech.powerjob.common.utils.CollectionUtils;
import tech.powerjob.server.solon.extension.alarm.AlarmTarget;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AlarmUtils
 *
 * @author tjq
 * @since 2023/7/31
 */
public class AlarmUtils {

    public static List<AlarmTarget> convertUserInfoList2AlarmTargetList(List<AnUser> userInfoDOS) {
        if (CollectionUtils.isEmpty(userInfoDOS)) {
            return Collections.emptyList();
        }
        return userInfoDOS.stream().map(AlarmUtils::convertUserInfo2AlarmTarget).collect(Collectors.toList());
    }

    public static AlarmTarget convertUserInfo2AlarmTarget(AnUser user) {
        AlarmTarget alarmTarget = new AlarmTarget();
        alarmTarget.setPhone(user.getMobile());

        alarmTarget.setName(user.getName());
        return alarmTarget;
    }

}
