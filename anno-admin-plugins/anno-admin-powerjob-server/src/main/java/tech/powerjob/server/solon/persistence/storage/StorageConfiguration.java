package tech.powerjob.server.solon.persistence.storage;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.AppContext;
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
    @Inject
    AppContext appContext;


    @Bean
    @Condition(onProperty = "${oms.storage.dfs.mysql_series.enable} = true")
    public DFsService initDbFs() {
        MySqlSeriesDfsService mySqlSeriesDfsService = new MySqlSeriesDfsService();
        mySqlSeriesDfsService.init(appContext);
        return mySqlSeriesDfsService;
    }

    @Bean
    @Condition(onProperty = "{oms.storage.dfs.alioss.enable} = true")
    public DFsService initAliOssFs() {
        AliOssService aliOssService = new AliOssService();
        aliOssService.init(appContext);
        return aliOssService;
    }

    @Bean
    @Condition(onMissingBean = DFsService.class)
    public DFsService initEmptyDfs() {
        return new EmptyDFsService();
    }
}
