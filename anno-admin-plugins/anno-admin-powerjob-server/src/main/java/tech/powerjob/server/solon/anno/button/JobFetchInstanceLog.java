package tech.powerjob.server.solon.anno.button;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.anno.javacmd.JavaCmdParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

@Component
public class JobFetchInstanceLog implements JavaCmdSupplier {

    @Inject
    JobInstanceButtonService jobInstanceButtonService;

    @Override
    public String run(JavaCmdParam param) {
        jobInstanceButtonService.fetchInstanceLog(param);
        return "运行成功";
    }
}
