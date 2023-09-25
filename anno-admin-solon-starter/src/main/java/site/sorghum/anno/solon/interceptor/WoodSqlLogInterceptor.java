package site.sorghum.anno.solon.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.noear.solon.Solon;
import org.noear.wood.Command;
import org.noear.wood.ext.Act1;
import site.sorghum.anno._common.config.AnnoProperty;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/4 12:59
 */
@Slf4j
public class WoodSqlLogInterceptor implements Act1<Command> {

    private AnnoProperty annoProperty;

    private final TablesNamesFinder tablesNamesFinder = new TablesNamesFinder() {
        @Override
        protected String extractTableName(Table table) {
            return table.getName();
        }
    };

    public WoodSqlLogInterceptor() {
        Solon.context().getBeanAsync(AnnoProperty.class, bean -> {
            this.annoProperty = bean;
        });
    }

    @Override
    public void run(Command cmd) {
        if (!annoProperty.isShowSql()) {
            return;
        }
        String sql = cmd.text;
        List<String> skipTableList = annoProperty.getSkipTable();
        if (CollUtil.isNotEmpty(skipTableList)) {
            try {
                Statement parse = CCJSqlParserUtil.parse(sql);

                List<String> tableList = tablesNamesFinder.getTableList(parse);
                if (tableList.stream().anyMatch(tableName -> StrUtil.containsAnyIgnoreCase(tableName,
                    ArrayUtil.toArray(skipTableList, String.class)))) {
                    return;
                }

            } catch (JSQLParserException e) {
                printSql(cmd);
                return;
            }
        }

        printSql(cmd);
    }

    private void printSql(Command cmd) {
        log.debug("sql: {}, cost: {}ms", cmd.text, cmd.timespan());
        log.debug("var: {}", cmd.paramMap());
    }


}
