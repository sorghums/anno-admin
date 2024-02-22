package site.sorghum.anno.plugin.service;

import cn.hutool.core.io.resource.Resource;

import java.io.IOException;
import java.util.Map;

public interface AnSqlService {

    void runSql(Map<String, Object> data);

    /**
     * 执行资源文件中的sql，一般是 Jar 包中的 sql 文件
     *
     * @param resource 资源文件
     */
    void runResourceSql(Resource resource) throws IOException;

}
