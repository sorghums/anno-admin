package site.sorghum.anno.ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.wood.DbContext;
import org.noear.wood.WoodConfig;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.util.JSqlParserUtil;
import site.sorghum.anno.util.ScriptUtils;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author songyinyin
 * @since 2023/7/9 11:49
 */
@Component
public class InitDataService {

  @Db
  DbContext dbContext;

  public static Set<String> systemFields = Set.of("create_time", "create_by", "update_time", "update_by", "del_flag");

  public void init() throws Exception {
    // wood 设置
    WoodConfig.isSelectItemEmptyAsNull = true;
    WoodConfig.isUsingValueNull = true;

    // 初始化 init.sql
    initSql();


  }

  /**
   * 读取 init.sql 文件，对比数据库中已有的数据，初始化数据或者升级历史数据
   */
  public void initSql() throws Exception {
    URL resource = ResourceUtil.getResource(InitDdlAndDateService.class.getClassLoader(), "initdata/init.sql");

    List<String> statements = ScriptUtils.getStatements(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));

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
        itemsList.accept(new ExpressionVisitorAdapter() {
          @Override
          public void visit(ExpressionList expressionList) {
            Map<String, Object> row = new LinkedHashMap<>();
            boolean isSingleValue = false;
            for (int i = 0; i < expressionList.getExpressions().size(); i++) {
              Expression expression = expressionList.getExpressions().get(i);

              if (ReflectUtil.hasField(expression.getClass(), "value")) { // values 后只有一行数据
                isSingleValue = true;
                Object value = JSqlParserUtil.getExpressionValue(expression);
                row.put(indexToColumn.get(i), value);
              } else { // values 后有多行数据
                expression.accept(new ColumnExpressionVisitor(list, indexToColumn));
              }
            }
            if (isSingleValue) {
              list.add(row);
            }
          }
        });

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
  }
}
