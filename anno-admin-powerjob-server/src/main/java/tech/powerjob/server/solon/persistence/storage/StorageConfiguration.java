package tech.powerjob.server.solon.persistence.storage;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import tech.powerjob.server.solon.extension.dfs.DFsService;
import tech.powerjob.server.solon.persistence.storage.impl.AliOssService;
import tech.powerjob.server.solon.persistence.storage.impl.EmptyDFsService;
import tech.powerjob.server.solon.persistence.storage.impl.MySqlSeriesDfsService;

/**
 * 初始化内置的存储服务
 *
 * @author tjq
 * @since 2023/7/30
 */
@Configuration
public class StorageConfiguration {


    @Bean
    @Condition(onProperty = "oms.storage.dfs.mysql_series")
    public DFsService initDbFs() {
        return new MySqlSeriesDfsService();
    }

    @Bean
    @Condition(onProperty = "oms.storage.dfs.alioss")
    public DFsService initAliOssFs() {
        return new AliOssService();
    }

    @Bean
    @Condition(onMissingBean = DFsService.class)
    public DFsService initEmptyDfs() {
        return new EmptyDFsService();
    }
}
