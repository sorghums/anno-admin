package site.sorghum.anno._metadata;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/9/15 10:37
 */
@Component
@org.springframework.stereotype.Component
public class PermissionContext implements MetadataContext {

    /**
     * key=className#methodName, value=permissionCode
     */
    public Map<String, String> javaCmdPermissionMap = new HashMap<>();

    /**
     * key=permissionCode, value=permissionName
     */
    public Map<String, String> permissionCodeAndNameMap = new HashMap<>();

    @Override
    public void refresh(List<AnEntity> allEntities) {
        javaCmdPermissionMap.clear();
        permissionCodeAndNameMap.clear();

        for (AnEntity anEntity : allEntities) {
            if (StrUtil.isBlank(anEntity.getPermissionCode())) {
                continue;
            }
            // 默认权限
            permissionCodeAndNameMap.put(joinPermission(anEntity.getPermissionCode(), PermissionProxy.VIEW), joinPermission(anEntity.getTitle(), PermissionProxy.VIEW_TRANSLATE));
            permissionCodeAndNameMap.put(joinPermission(anEntity.getPermissionCode(), PermissionProxy.ADD), joinPermission(anEntity.getTitle(), PermissionProxy.ADD_TRANSLATE));
            permissionCodeAndNameMap.put(joinPermission(anEntity.getPermissionCode(), PermissionProxy.UPDATE), joinPermission(anEntity.getTitle(), PermissionProxy.UPDATE_TRANSLATE));
            permissionCodeAndNameMap.put(joinPermission(anEntity.getPermissionCode(), PermissionProxy.DELETE), joinPermission(anEntity.getTitle(), PermissionProxy.DELETE_TRANSLATE));

            List<AnButton> tableButtons = anEntity.getTableButtons();
            if (CollectionUtil.isNotEmpty(tableButtons)) {
                for (AnButton tableButton : tableButtons) {
                    if (StrUtil.isBlank(tableButton.getPermissionCode())) {
                        continue;
                    }
                    dealButton(anEntity, tableButton);
                }
            }
            List<AnColumnButton> columnButtons = anEntity.getColumnButtons();
            if (CollectionUtil.isNotEmpty(columnButtons)) {
                for (AnColumnButton columnButton : columnButtons) {
                    if (StrUtil.isBlank(columnButton.getPermissionCode())) {
                        continue;
                    }
                    dealButton(anEntity, columnButton);
                }
            }
        }
    }

    private void dealButton(AnEntity anEntity, AnButton tableButton) {
        String joinPermissionCode = joinPermission(anEntity.getPermissionCode(), tableButton.getPermissionCode());
        String joinPermissionName = joinPermission(anEntity.getTitle(), tableButton.getName());
        permissionCodeAndNameMap.put(joinPermissionCode, joinPermissionName);
        if (tableButton.getJavaCmdEnable()) {
            javaCmdPermissionMap.put(getPermissionKey(tableButton.getJavaCmdData().getJavaCmdBeanClass(), tableButton.getJavaCmdData().getJavaCmdMethodName()), tableButton.getPermissionCode());
        }
    }

    /**
     * 根据执行方法，获取对应的权限码
     */
    public String getPermissionCode(String className, String methodName) {
        return javaCmdPermissionMap.get(getPermissionKey(className, methodName));
    }

    /**
     * 根据权限码，获取对应的权限名称
     */
    public String getPermissionName(String permissionCode) {
        return permissionCodeAndNameMap.getOrDefault(permissionCode, permissionCode);
    }

    public String joinPermission(String... permissionList) {
        StringBuilder sb = new StringBuilder();
        for (String permissionCode : permissionList) {
            sb.append(permissionCode).append(":");
        }
        return sb.toString();
    }

    public String getPermissionKey(Class<?> javaCmdBeanClass, String javaCmdMethodName) {
        return getPermissionKey(javaCmdBeanClass.getName(), javaCmdMethodName);
    }

    public String getPermissionKey(String javaCmdBeanClassName, String javaCmdMethodName) {
        return javaCmdBeanClassName + "#" + javaCmdMethodName;
    }


}
