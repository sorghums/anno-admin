package site.sorghum.anno.plugin.javacmd;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.plugin.service.AnSqlService;

import java.util.Map;

@Named
public class RunSqlJavaCmdSupplier implements JavaCmdSupplier {

    @Inject
    AnSqlService anSqlService;

    @Override
    public String run(Map<String, Object> param) {
        anSqlService.runSql(param);
        return "执行成功";
    }
}
