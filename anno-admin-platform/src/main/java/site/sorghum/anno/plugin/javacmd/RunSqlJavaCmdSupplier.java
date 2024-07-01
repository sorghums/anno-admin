package site.sorghum.anno.plugin.javacmd;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.entity.CommenParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.plugin.service.AnSqlService;

@Named
public class RunSqlJavaCmdSupplier implements JavaCmdSupplier {

    @Inject
    AnSqlService anSqlService;

    @Override
    public String run(CommenParam param) {
        anSqlService.runSql(param);
        return "执行成功";
    }
}
