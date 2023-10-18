package site.sorghum.anno.anno.dami;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;
import org.noear.dami.bus.impl.TopicDispatcherDefault;

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
        //开始监视...
        StopWatch stopWatch = new StopWatch();

        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            TopicListener<Payload<C, R>> listener = targets.get(i).getListener();

            //发送前监视...
            stopWatch.start(getListenerName(listener));

            listener.onEvent(payload);
            //发送后监视...
            stopWatch.stop();
        }

        //结速监视...
        if (stopWatch.getTotalTimeMillis() > 100) {
            log.info("{}", stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        }
    }

    protected String getListenerName(TopicListener<Payload<C, R>> listener) {

        if (listener.getClass().isAssignableFrom(MethodTopicListener.class)) {
            return listener.toString();
        }
        return listener.getClass().getSimpleName();
    }
}
