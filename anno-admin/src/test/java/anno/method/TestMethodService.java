package anno.method;

import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.MethodTemplate;
import site.sorghum.anno.method.route.EntityMethodRoute;

/**
 * @author songyinyin
 * @since 2024/1/16 12:24
 */
@MethodTemplate(route = EntityMethodRoute.class)
public interface TestMethodService {

    void delete(DbCriteria criteria);
}
