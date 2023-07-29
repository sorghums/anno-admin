package site.sorghum.anno.modular.ddl;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import site.sorghum.anno.common.util.JSqlParserUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于访问 values 后的内容
 *
 * @author songyinyin
 * @since 2023/7/18 13:57
 */
public class ExpressionListVisitor extends ExpressionVisitorAdapter {

    /**
     * 用于存储 values 后的数据
     */
    private final List<Map<String, Object>> list;

    /**
     * key=values 后的列索引，value=列名
     */
    private final Map<Integer, String> indexToColumn;

    public ExpressionListVisitor(List<Map<String, Object>> list, Map<Integer, String> indexToColumn) {
        this.list = list;
        this.indexToColumn = indexToColumn;
    }

    @Override
    public void visit(ExpressionList expressionList) {
        Map<String, Object> row = new LinkedHashMap<>();
        boolean isSingleValue = false;
        for (int i = 0; i < expressionList.getExpressions().size(); i++) {
            Expression expression = expressionList.getExpressions().get(i);
            if (expression instanceof RowConstructor) {
                expression.accept(new ColumnExpressionVisitor(list, indexToColumn));
            } else {
                isSingleValue = true;
                Object value = JSqlParserUtil.getExpressionValue(expression);
                row.put(indexToColumn.get(i), value);
            }
        }
        if (isSingleValue) {
            list.add(row);
        }
    }
}
