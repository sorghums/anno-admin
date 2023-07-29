package site.sorghum.anno.common.util;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;

/**
 * db工具类
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Component
public class DbContextUtil {
    @Db
    private DbContext dbContext;

    private static DbContext staticDbContext;

    @Init
    public void init() {
        staticDbContext = dbContext;
    }

    public static DbContext dbContext() {
        return staticDbContext;
    }

}
