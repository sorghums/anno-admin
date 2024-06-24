package tech.powerjob.server.solon.anno.button;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.anno.javacmd.JavaCmdParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import tech.powerjob.server.solon.core.service.JobService;

import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/9/7 11:27
 */
@Component
public class JobButtonService implements JavaCmdSupplier {

    @Inject
    JobService jobService;

    @Override
    public String run(JavaCmdParam param) {
        runJob(param);
        return "运行成功";
    }

    /**
     * 立即执行任务
     */
    public void runJob(Map<String, Object> props) {
        String appId = (String) props.get("appId");
        String jobId = (String) props.get("id");
        jobService.runJob(appId, jobId, null, null);
    }
}
