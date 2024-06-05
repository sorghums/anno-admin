package tech.powerjob.server.solon.anno.button;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.util.Map;

@Component
public class JobFetchInstanceLog implements JavaCmdSupplier {

    @Inject
    JobInstanceButtonService jobInstanceButtonService;

    @Override
    public String run(Map<String, Object> param) {
        jobInstanceButtonService.fetchInstanceLog(param);
        return "运行成功";
    }
}
