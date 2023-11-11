package site.sorghum.anno.anno.dami;

import lombok.extern.slf4j.Slf4j;
import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;
import org.noear.dami.bus.impl.TopicDispatcherDefault;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno.anno.util.ReentrantStopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author songyinyin
 * @since 2023/10/18 10:30
 */
@Slf4j
public class TopicDispatcherMonitor<C, R> extends TopicDispatcherDefault<C, R> {

    @Override
    protected void doDispatch(Payload<C, R> payload, List<TopicListenerHolder<C, R>> targets) throws Throwable {
        ReentrantStopWatch stopWatch;
        if (AnnoContextUtil.hasContext()) {
            //开始监视...
            stopWatch = AnnoContextUtil.getContext().getStopWatch();

        } else {
            stopWatch = new ReentrantStopWatch(payload.getTopic());
        }
        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            TopicListener<Payload<C, R>> listener = targets.get(i).getListener();

            //发送前监视...
            stopWatch.start(getListenerName(payload, listener));

            listener.onEvent(payload);
            //发送后监视...
            stopWatch.stop();
        }
        //结速监视...
        AnnoProperty annoProperty = AnnoBeanUtils.getBean(AnnoProperty.class);
        if (!AnnoContextUtil.hasContext() && stopWatch.getTotalTimeMillis() > annoProperty.getDetailLogThreshold()) {
            log.info("{}", stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        }
    }

    protected String getListenerName(Payload<C, R> payload, TopicListener<Payload<C, R>> listener) {
        String topic = payload.getTopic();

        String gussName;
        String[] split = topic.split("\\.");
        if (split.length > 1) {
            gussName = split[split.length - 2];
        } else {
            gussName = topic;
        }

        String str = "%s@%s";
        if (listener.getClass().isAssignableFrom(MethodTopicListener.class)) {
            return str.formatted(gussName, listener.toString());
        }
        return str.formatted(gussName, listener.getClass().getSimpleName());
    }
}
