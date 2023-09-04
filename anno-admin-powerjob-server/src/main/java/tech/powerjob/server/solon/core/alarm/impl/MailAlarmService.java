package tech.powerjob.server.solon.core.alarm.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import tech.powerjob.server.solon.extension.alarm.Alarm;
import tech.powerjob.server.solon.extension.alarm.AlarmTarget;
import tech.powerjob.server.solon.extension.alarm.Alarmable;
import tech.powerjob.common.utils.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 邮件通知服务
 *
 * @author tjq
 * @since 2020/4/30
 */
@Slf4j
@Component
public class MailAlarmService implements Alarmable {

    @Inject(required = false)
    private Mailer javaMailSender;

    @Inject("${simplejavamail.defaults.from.name:''}")
    private String from;

    @Override
    public void onFailed(Alarm alarm, List<AlarmTarget> targetUserList) {
        if (CollectionUtils.isEmpty(targetUserList) || javaMailSender == null || StringUtils.isEmpty(from)) {
            return;
        }

        try {
            Email mail = EmailBuilder.startingBlank()
                .toMultiple(targetUserList.stream().map(AlarmTarget::getEmail).filter(Objects::nonNull).toArray(String[]::new))
                .withSubject(alarm.fetchTitle())
                .appendText(alarm.fetchContent())
                .buildEmail();
            javaMailSender.sendMail(mail);//同步发送邮件
        } catch (Exception e) {
            log.warn("[MailAlarmService] send mail failed, reason is {}", e.getMessage());
        }
    }

}
