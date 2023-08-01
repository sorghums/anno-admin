package site.sorghum.anno.anno.proxy;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

/**
 * 许可代理
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Named
public class PermissionProxy {

    @Inject
    MetadataManager metadataManager;

    public static final String ADD = "add";

    public static final String ADD_TRANSLATE = "增加";

    public static final String UPDATE = "update";

    public static final String UPDATE_TRANSLATE = "修改";

    public static final String DELETE = "delete";

    public static final String DELETE_TRANSLATE = "删除";

    public void fetchPermission(Object obj) {
        fetchPermission(obj.getClass());
    }

    public void fetchPermission(Class<?> clazz) {
        if (isSystemRun()) {
            return;
        }
        AnEntity anEntity = metadataManager.getEntity(clazz);
        boolean enable = anEntity.isEnablePermission();
        if (!enable) {
            return;
        }
        String baseCode = anEntity.getPermissionCode();
        // 校验权限
        StpUtil.checkPermission(baseCode);
    }

    public void addPermission(Object obj) {
        addPermission(obj.getClass());
    }

    public void addPermission(Class<?> clazz) {
        checkPermission(metadataManager.getEntity(clazz), ADD);
    }

    public void updatePermission(Object obj) {
        updatePermission(obj.getClass());
    }

    public void updatePermission(Class<?> clazz) {
        checkPermission(metadataManager.getEntity(clazz), UPDATE);
    }

    public void deletePermission(Object obj) {
        deletePermission(obj.getClass());
    }

    public void deletePermission(Class<?> clazz) {
        checkPermission(metadataManager.getEntity(clazz), DELETE);
    }

    public void checkPermission(AnEntity anEntity, String code) {
        if (isSystemRun() || StrUtil.isBlank(code)) {
            return;
        }
        boolean enable = anEntity.isEnablePermission();
        if (!enable) {
            return;
        }
        String baseCode = anEntity.getPermissionCode();
        // 校验权限
        StpUtil.checkPermission(baseCode + ":" + code);
    }

    private boolean isSystemRun() {
        SaRequest request = SaHolder.getRequest();
        return request == null || request.getSource() == null;
    }
}