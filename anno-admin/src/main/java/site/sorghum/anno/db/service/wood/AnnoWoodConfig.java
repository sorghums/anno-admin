package site.sorghum.anno.db.service.wood;

import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.wood.WoodConfig;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno.anno.util.ReentrantStopWatch;

@Named
public class AnnoWoodConfig {

    /**
     * 初始化wood配置
     */
    public void init(){
        WoodConfig.onExecuteBef((cmd) -> {
            if (AnnoContextUtil.hasContext()) {
                ReentrantStopWatch stopWatch = AnnoContextUtil.getContext().getStopWatch();
                String taskName;
                if (CollUtil.isEmpty(cmd.paramMap())) {
                    taskName = cmd.text;
                } else {
                    taskName = String.format("%s\n%s params ==> %s", cmd.text, " ".repeat(16), cmd.paramMap());
                }
                stopWatch.start(taskName);
            }
            return true;
        });
        WoodConfig.onExecuteAft((cmd) -> {
            if (AnnoContextUtil.hasContext()) {
                ReentrantStopWatch stopWatch = AnnoContextUtil.getContext().getStopWatch();
                stopWatch.stop();
            }
        });
        WoodConfig.primaryKeyStrategy = new AnnoPrimaryKeyStrategy();
        WoodConfig.namingStrategy = new AnnoNamingStrategy();
    }
}
