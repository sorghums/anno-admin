package site.sorghum.anno.modular.anno.proxy;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.stp.StpUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

/**
 * 许可代理
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Component
public class PermissionProxy {

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
        if (isSystemRun()){
            return;
        }
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
        checkPermission(getAnnoPermission(clazz), ADD);
    }

    public void updatePermission(Object obj) {
        updatePermission(obj.getClass());
    }

    public void updatePermission(Class<?> clazz) {
        checkPermission(getAnnoPermission(clazz), UPDATE);
    }

    public void deletePermission(Object obj) {
        deletePermission(obj.getClass());
    }

    public void deletePermission(Class<?> clazz) {
        checkPermission(getAnnoPermission(clazz), DELETE);
    }

    private void checkPermission(AnnoPermission annoPermission, String delete) {
        if (isSystemRun()) {
            return;
        }
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        StpUtil.checkPermission(baseCode + ":" + delete);
    }

    public AnnoPermission getAnnoPermission(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        return annoMain.annoPermission();
    }

    private boolean isSystemRun(){
        SaRequest request = SaHolder.getRequest();
        if (request != null && request.getSource() != null) {
            return false;
        }
        return true;
    }
}
