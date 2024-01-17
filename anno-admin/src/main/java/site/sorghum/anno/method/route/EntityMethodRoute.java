package site.sorghum.anno.method.route;

import cn.hutool.core.util.ClassUtil;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.MTContext;

/**
 * @author songyinyin
 * @since 2024/1/17 10:37
 */
public class EntityMethodRoute implements MethodRoute{
    @Override
    public String[] route(MTContext context) {
        Object arg = context.getArgs()[0];
        String subPath = null;
        if (arg instanceof DbCriteria) {
            subPath = ((DbCriteria) arg).getEntityName();
        } else if (!ClassUtil.isJdkClass(arg.getClass())) {
            subPath = arg.getClass().getSimpleName();
        }
        if (subPath == null) {
            return new String[]{"all"};
        }
        return new String[]{"all", subPath};
    }
}
