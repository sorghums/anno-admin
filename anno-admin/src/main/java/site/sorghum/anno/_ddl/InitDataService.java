package site.sorghum.anno._ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.noear.wood.DbContext;
import org.noear.wood.WoodConfig;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.ScriptUtils;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 添加和升级预置数据
 *
 * @author songyinyin
 * @since 2023/7/9 11:49
 */
@Slf4j
@Named
public class InitDataService {

    @Db
    DbContext dbContext;

    @Inject
    AnnoProperty annoProperty;

    public static Set<String> systemFields = Set.of("create_time", "create_by", "update_time", "update_by", "del_flag");

    public void init(URL resource) throws Exception {
        // wood 设置
        WoodConfig.isSelectItemEmptyAsNull = true;
        WoodConfig.isUsingValueNull = true;

        // 初始化 init.sql
        if (annoProperty.getIsAutoMaintainInitData()) {
            initSql(resource);
        }


    }

    /**
     * 对比数据库中已有的数据，初始化数据或者升级历史数据
     *
     * @param resource 预置数据文件
     */
    private void initSql(URL resource) throws Exception {
        StopWatch stopWatch = new StopWatch("init sql");
        stopWatch.start("read sql");
        List<String> statements = ScriptUtils.getStatements(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
        stopWatch.stop();

        stopWatch.start("parse & execute sql");
        for (String statement : statements) {

            Statement parse = CCJSqlParserUtil.parse(statement);
            // 先只处理
            if (parse instanceof Insert) {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<Integer, String> indexToColumn = new HashMap<>();
                for (int index = 0; index < ((Insert) parse).getColumns().size(); index++) {
                    indexToColumn.put(index, ((Insert) parse).getColumns().get(index).getColumnName());
                }
                ItemsList itemsList = ((Insert) parse).getItemsList();
                itemsList.accept(new ExpressionListVisitor(list, indexToColumn));

                for (Map<String, Object> map : list) {
                    String id = (String) map.get("id");
                    if (StrUtil.isBlank(id)) {
                        continue;
                    }
                    String tableName = ((Insert) parse).getTable().getName();

                    Set<String> keys = map.keySet().stream().filter(k -> !systemFields.contains(k)).collect(Collectors.toSet());
                    Map<String, Object> existMap = dbContext.table(tableName).whereEq("id", id).selectMap(String.join(",", keys));
                    if (CollUtil.isEmpty(existMap)) {
                        dbContext.table(tableName).setMap(map).insert();
                    } else {
                        systemFields.forEach(map::remove);
                        for (String key : keys) {
                            map.remove(key, existMap.get(key));
                        }
                        if (!map.isEmpty()) {
                            dbContext.table(tableName).setMap(map).whereEq("id", id).update();
                        }
                    }
                }
            }
        }
        stopWatch.stop();
        log.info("init sql({}) finished, time: {}ms", resource.getPath(), stopWatch.getTotal(TimeUnit.MILLISECONDS));
        if (log.isDebugEnabled()) {
            log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        }
    }
}
