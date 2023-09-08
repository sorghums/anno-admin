package site.sorghum.anno;


import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.db.interfaces.AnnoAdminCoreFunctions;
import site.sorghum.anno.plugin.AnnoPlugin;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Named
public class AnnoCorePlugin extends AnnoPlugin {

    public AnnoCorePlugin() {
        super("核心插件", "包含基础的页面生成功能 & 数据库查询功能");
    }

    @Override
    public void run() {
        AnnoAdminCoreFunctions.tableParamFetchFunction = AnnoBeanUtils.metadataManager()::getTableParam;
        super.run();
    }
}
