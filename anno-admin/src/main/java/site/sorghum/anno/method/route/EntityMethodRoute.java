package site.sorghum.anno.method.route;

import cn.hutool.core.util.ClassUtil;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
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
            String entityName = ((DbCriteria) arg).getEntityName();
            AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(entityName);
            return argClass2Router(entity.getClazz());
        } else if (!ClassUtil.isJdkClass(arg.getClass())) {
            return argClass2Router(arg.getClass());
        }
        return new String[]{"all"};
    }

    private static String[] argClass2Router(Class<?> argClass) {
        List<String> route = new ArrayList<>();
        // 循环直到父类是Object
        while (argClass.getSuperclass() != Object.class) {
            route.add(argClass.getSimpleName());
            argClass = argClass.getSuperclass();
        }
        route.add(0, "all");
        return route.toArray(new String[0]);
    }
}
