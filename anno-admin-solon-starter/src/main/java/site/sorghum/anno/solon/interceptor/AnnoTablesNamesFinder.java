package site.sorghum.anno.solon.interceptor;

import cn.hutool.core.util.StrUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;

/**
 * @author songyinyin
 * @since 2023/10/4 19:10
 */
@Component
public class AnnoTablesNamesFinder extends TablesNamesFinder implements LifecycleBean {

    private String delimiterToken;

    @Db
    DbContext dbContext;

    @Override
    public void start() throws Throwable {
        this.delimiterToken ="`";
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
