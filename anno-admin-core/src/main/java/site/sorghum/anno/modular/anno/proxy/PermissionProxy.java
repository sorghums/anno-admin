package site.sorghum.anno.modular.anno.proxy;

import cn.dev33.satoken.stp.StpUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

/**
 * 许可代理
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Component
public class PermissionProxy {
    @Inject
    AnnoService annoService;

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
        AnnoPermission annoPermission = getAnnoPermission(clazz);
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        StpUtil.checkPermission(baseCode);
    }

    public void addPermission(Object obj) {
        addPermission(obj.getClass());
    }

    public void addPermission(Class<?> clazz) {
        AnnoPermission annoPermission = getAnnoPermission(clazz);
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        StpUtil.checkPermission(baseCode + ":" + ADD);
    }

    public void updatePermission(Object obj) {
        updatePermission(obj.getClass());
    }

    public void updatePermission(Class<?> clazz) {
        AnnoPermission annoPermission = getAnnoPermission(clazz);
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        StpUtil.checkPermission(baseCode + ":" + UPDATE);
    }

    public void deletePermission(Object obj) {
        deletePermission(obj.getClass());
    }

    public void deletePermission(Class<?> clazz) {
        AnnoPermission annoPermission = getAnnoPermission(clazz);
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        StpUtil.checkPermission(baseCode + ":" + DELETE);
    }

    public AnnoPermission getAnnoPermission(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        return annoMain.annoPermission();
    }
}
