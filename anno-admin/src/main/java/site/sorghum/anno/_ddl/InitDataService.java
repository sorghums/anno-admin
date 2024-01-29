package site.sorghum.anno._ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.util.ScriptUtils;
import site.sorghum.anno.db.service.DbService;

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
    DbService dbService;

    public static Set<String> systemFields = Set.of("create_time", "create_by", "update_time", "update_by", "del_flag");

    public void init(URL resource) throws Exception {
        // 初始化 init.sql
        initSql(resource);
    }

    /**
     * 对比数据库中已有的数据，初始化数据或者升级历史数据
     *
     * @param resource 预置数据文件
     */
    private void initSql(URL resource) throws Exception {
        StopWatch stopWatch = new StopWatch("init sql");
        stopWatch.start("read sql");
        String content = IoUtil.read(resource.openStream(), StandardCharsets.UTF_8);
        stopWatch.stop();

        stopWatch.start("parse & execute sql");
        dbService.executeSql(content);
        stopWatch.stop();
        log.info("init sql({}) finished, time: {}ms", resource.getPath(), stopWatch.getTotal(TimeUnit.MILLISECONDS));
        if (log.isDebugEnabled()) {
            log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        }
    }
}
