package site.sorghum.anno.method.route;

import cn.hutool.core.util.ClassUtil;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.MTContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2024/1/17 10:37
 */
public class EntityMethodRoute implements MethodRoute {
    @Override
    public String[] route(MTContext context) {
        Object arg = context.getArgs()[0];
        String subPath = null;
        if (arg instanceof DbCriteria) {
            subPath = ((DbCriteria) arg).getEntityName();
        } else if (!ClassUtil.isJdkClass(arg.getClass())) {
            List<String> route = new ArrayList<>();
            Class<?> argClass = arg.getClass();
            // 循环直到父类是Object
            while (true) {
                if (argClass.getSuperclass() == Object.class) {
                    break;
                }
                route.add(argClass.getSimpleName());
                argClass = argClass.getSuperclass();
            }
            route.add(0, "all");
            return route.toArray(new String[0]);
        }
        if (subPath == null) {
            return new String[]{"all"};
        }
        return new String[]{"all", subPath};
    }
}
