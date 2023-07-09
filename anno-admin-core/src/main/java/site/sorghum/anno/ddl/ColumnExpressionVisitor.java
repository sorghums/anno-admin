package site.sorghum.anno.ddl;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import site.sorghum.anno.util.JSqlParserUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/7/8 17:44
 */
public class ColumnExpressionVisitor extends ExpressionVisitorAdapter {

  private final List<Map<String, Object>> list;

  private final Map<Integer, String> indexToColumn;

  public ColumnExpressionVisitor(List<Map<String, Object>> list, Map<Integer, String> indexToColumn) {
    this.list = list;
    this.indexToColumn = indexToColumn;
  }

  @Override
  public void visit(RowConstructor rowConstructor) {
    ExpressionList exprList = rowConstructor.getExprList();
    List<Expression> expressions = exprList.getExpressions();
    Map<String, Object> columnToValue = new LinkedHashMap<>();
    for (int i = 0; i < expressions.size(); i++) {
      Expression expression = expressions.get(i);
      columnToValue.put(indexToColumn.get(i), JSqlParserUtil.getExpressionValue(expression));
    }
    list.add(columnToValue);

  }
}
