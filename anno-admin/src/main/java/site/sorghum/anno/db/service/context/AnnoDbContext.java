package site.sorghum.anno.db.service.context;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.AnnoBeanUtils;

import java.util.function.Supplier;

/**
 * Anno数据库上下文
 *
 * @author Sorghum
 * @since 2024/11/18
 */
@Getter
@Slf4j
@Named
public class AnnoDbContext {

    @Db
    DbContext dbContext;

    private static final ThreadLocal<DbContext> dbContextThreadLocal = new ThreadLocal<>();

    /**
     * 数据库上下文
     *
     * @param name 名称
     * @return {@link DbContext }
     */
    private static DbContext _dbContext(String name) {
        if (StrUtil.isBlank(name)) {
            return AnnoBeanUtils.getBean(AnnoDbContext.class).getDbContext();
        }
        return AnnoBeanUtils.getBean(name);
    }

    private static boolean hasThreadLocal() {
        return dbContextThreadLocal.get() != null;
    }
    /**
     * 获取当前线程的数据库上下文对象
     *
     * @return 返回当前线程的数据库上下文对象
     */
    public static DbContext dbContext() {
        if (hasThreadLocal()) {
            return dbContextThreadLocal.get();
        }
        log.warn("当前线程没有设置数据库上下文，已使用默认数据源.");
        return _dbContext(null);
    }

    /**
     * 动态设置数据库上下文
     *
     * @param name     数据库名称
     * @param supplier 需要执行的代码块
     */
    public static<T> T dynamicDbContext(String name, Supplier<T> supplier) {
        boolean hasThreadLocal = hasThreadLocal();
        try {
            if (!hasThreadLocal) {
                dbContextThreadLocal.set(_dbContext(name));
            }
            return supplier.get();
        } finally {
            if (!hasThreadLocal) {
                dbContextThreadLocal.remove();
            }
        }
    }
}
