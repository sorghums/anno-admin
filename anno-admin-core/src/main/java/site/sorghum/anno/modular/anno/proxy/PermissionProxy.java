package site.sorghum.anno.modular.anno.proxy;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.IdUtil;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.system.anno.SysPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Init
    public void init() {
        // 初始化的时候，进行Db的注入
        List<AnnoPermission> annoPermissions = new ArrayList<>();
        Collection<Class<?>> classes = AnnoClazzCache.fetchAllClazz();
        for (Class<?> aClass : classes) {
            // 获取类上注解
            AnnoMain annoMain = AnnotationUtil.getAnnotation(aClass, AnnoMain.class);
            AnnoPermission annoPermission = annoMain.annoPermission();
            if (annoPermission.enable()) {
                annoPermissions.add(annoPermission);
            }
        }
        // 插入数据库
        annoPermissions.forEach(
                annoPermission -> {
                    String baseCode = annoPermission.baseCode();
                    String baseName = annoPermission.baseCodeTranslate();

                    SysPermission sysPermission = annoService.queryById(SysPermission.class, baseCode);
                    if (sysPermission != null && sysPermission.getId() != null) {
                        return;
                    }

                    SysPermission basePermission = new SysPermission();
                    basePermission.setId(baseCode);
                    basePermission.setCode(baseCode);
                    basePermission.setName(baseName);
                    basePermission.setDelFlag(0);

                    annoService.onlySave(basePermission);


                    // 新增
                    String addCode = baseCode + ":" + ADD;
                    String addName = baseName + ":" + ADD_TRANSLATE;

                    SysPermission addPermission = new SysPermission();
                    addPermission.setId(addCode);
                    addPermission.setParentId(baseCode);
                    addPermission.setCode(addCode);
                    addPermission.setName(addName);
                    addPermission.setDelFlag(0);

                    annoService.onlySave(addPermission);

                    // 修改
                    String updateCode = baseCode + ":" + UPDATE;
                    String updateName = baseName + ":" + UPDATE_TRANSLATE;

                    SysPermission updatePermission = new SysPermission();
                    updatePermission.setId(updateCode);
                    updatePermission.setParentId(baseCode);
                    updatePermission.setCode(updateCode);
                    updatePermission.setName(updateName);
                    updatePermission.setDelFlag(0);

                    annoService.onlySave(updatePermission);

                    // 删除
                    String deleteCode = baseCode + ":" + DELETE;
                    String deleteName = baseName + ":" + DELETE_TRANSLATE;

                    SysPermission deletePermission = new SysPermission();
                    deletePermission.setId(deleteCode);
                    deletePermission.setParentId(baseCode);
                    deletePermission.setCode(deleteCode);
                    deletePermission.setName(deleteName);
                    deletePermission.setDelFlag(0);

                    annoService.onlySave(deletePermission);
                }
        );
    }
}
