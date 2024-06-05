package site.sorghum.anno.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * 元对象处理程序
 *
 * @author Sorghum
 * @since 2023/03/10
 */
public class AnnoAdminMetaObjectHandler implements MetaObjectHandler {

    public static Supplier<String> userIdSupplier = () -> "";

    public static Supplier<String> orgIdSupplier = () -> "";

    @Override
    public void insertFill(MetaObject metaObject) {
        onlyInsert(metaObject);
        onlyUpdate(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        onlyUpdate(metaObject);
    }

    /**
     * 只有更新
     *
     * @param metaObject 元对象
     */
    private void onlyUpdate(MetaObject metaObject) {
        try {
            this.strictUpdateFill(metaObject, "updateBy", String.class, getUserId());
        } catch (Throwable ignore) {
        }
        try {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        } catch (Throwable ignore) {
        }
    }


    /**
     * 只有插入
     *
     * @param metaObject 元对象
     */
    private void onlyInsert(MetaObject metaObject) {
        try {
            this.strictInsertFill(metaObject, "createBt", String.class, getUserId());
        } catch (Throwable ignore) {
        }
        try {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        } catch (Throwable ignore) {
        }
        try {
            this.strictInsertFill(metaObject, "delFlag", Integer.class, 0);
        } catch (Throwable ignore) {
        }
        try {
            // 查询是否原来已经有值
            Object orgId = this.getFieldValByName("orgId", metaObject);
            if (orgId != null) {
                return;
            }
            this.strictInsertFill(metaObject, "orgId", String.class, getMainOrgId());
        } catch (Throwable ignore) {
        }
    }

    /**
     * 获取当前用户
     *
     * @return 用户名
     */
    private String getUserId() {
        try {
            return userIdSupplier.get();
        } catch (Throwable e) {
            return "unknown";
        }
    }

    /**
     * 获取当前用户主要使用组织
     *
     * @return 用户名
     */
    private String getMainOrgId() {
        try {
            return orgIdSupplier.get();
        } catch (Throwable e) {
            return "-1";
        }
    }
}