package site.sorghum.anno.common.util;

import cn.hutool.core.util.ReflectUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;

/**
 * @author songyinyin
 * @since 2023/7/9 13:16
 */
public class JSqlParserUtil {

  public static Object getExpressionValue(Expression expression) {
    if (expression instanceof NullValue) {
      return null;
    }
    return ReflectUtil.invoke(expression, "getValue");
  }
}
