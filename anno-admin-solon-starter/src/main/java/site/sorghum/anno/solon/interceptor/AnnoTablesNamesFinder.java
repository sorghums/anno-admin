package site.sorghum.anno.solon.interceptor;

import cn.hutool.core.util.StrUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.bean.InitializingBean;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._ddl.Platform;
import site.sorghum.anno._ddl.PlatformFactory;

/**
 * @author songyinyin
 * @since 2023/10/4 19:10
 */
@Component
public class AnnoTablesNamesFinder extends TablesNamesFinder implements InitializingBean {

    private String delimiterToken;

    @Db
    DbContext dbContext;

    @Inject
    PlatformFactory platformFactory;

    @Override
    public void afterInjection() throws Throwable {

        Platform instance = platformFactory.getPlatformInstance(dbContext.getMetaData());
        this.delimiterToken = instance.getDatabaseInfo().getDelimiterToken();
    }

    @Override
    protected String extractTableName(Table table) {
        String tableName = table.getName();
        if (delimiterToken != null && tableName.startsWith(delimiterToken)) {
            return tableName.replace(delimiterToken, StrUtil.EMPTY);
        }
        return tableName;
    }

}
