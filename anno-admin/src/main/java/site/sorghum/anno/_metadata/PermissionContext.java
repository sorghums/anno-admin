package site.sorghum.anno._metadata;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoChartFieldImpl;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author songyinyin
 * @since 2023/9/15 10:37
 */
@Named
public class PermissionContext implements MetadataContext {

    /**
     * key=className#methodName, value=permissionCode
     */
    private final Map<String, String> javaCmdPermissionMap = new HashMap<>();

    /**
     * key=permissionCode, value=permissionName
     */
    private final Map<String, String> permissionCodeAndNameMap = new HashMap<>();

    @Override
    public void refresh(List<AnEntity> allEntities) {
        javaCmdPermissionMap.clear();
        permissionCodeAndNameMap.clear();

        for (AnEntity anEntity : allEntities) {
            String baseCode = anEntity.getAnnoPermission().getBaseCode();
            if (StrUtil.isBlank(baseCode)) {
                continue;
            }

            // 默认权限
            permissionCodeAndNameMap.put(joinPermission(baseCode, PermissionProxy.VIEW), joinPermission(anEntity.getName(), PermissionProxy.VIEW_TRANSLATE));
            permissionCodeAndNameMap.put(joinPermission(baseCode, PermissionProxy.ADD), joinPermission(anEntity.getName(), PermissionProxy.ADD_TRANSLATE));
            permissionCodeAndNameMap.put(joinPermission(baseCode, PermissionProxy.UPDATE), joinPermission(anEntity.getName(), PermissionProxy.UPDATE_TRANSLATE));
            permissionCodeAndNameMap.put(joinPermission(baseCode, PermissionProxy.DELETE), joinPermission(anEntity.getName(), PermissionProxy.DELETE_TRANSLATE));

            // 表级按钮
            List<AnnoTableButtonImpl> tableButtons = anEntity.getTableButtons();
            if (CollectionUtil.isNotEmpty(tableButtons)) {
                for (AnnoTableButtonImpl tableButton : tableButtons) {
                    if (StrUtil.isBlank(tableButton.getPermissionCode())) {
                        continue;
                    }
                    dealButton(anEntity, tableButton);
                }
            }

            // 行级按钮
            List<AnnoButtonImpl> columnButtons = anEntity.getColumnButtons();
            if (CollectionUtil.isNotEmpty(columnButtons)) {
                for (AnnoButtonImpl columnButton : columnButtons) {
                    if (StrUtil.isBlank(columnButton.getPermissionCode())) {
                        continue;
                    }
                    dealButton(anEntity, columnButton);
                }
            }

            // 图表权限
            List<AnnoChartFieldImpl> chartFields = Arrays.asList(anEntity.getAnnoChart().getChartFields());
            if (CollectionUtil.isNotEmpty(chartFields)) {
                for (AnnoChartFieldImpl chartField : chartFields) {
                    if (StrUtil.isBlank(chartField.getPermissionCode())) {
                        continue;
                    }
                    dealButton(anEntity, chartField);
                }
            }
        }
    }

    private void dealButton(AnEntity anEntity, AnnoButtonImpl tableButton) {
        String joinPermissionCode = joinPermission(anEntity.getAnnoPermission().getBaseCode(), tableButton.getPermissionCode());
        String joinPermissionName = joinPermission(anEntity.getName(), tableButton.getName());
        permissionCodeAndNameMap.put(joinPermissionCode, joinPermissionName);
        if (tableButton.getJavaCmd().isEnable()) {
            javaCmdPermissionMap.put(getPermissionKey(tableButton.getJavaCmd().getRunSupplier(), "run"), tableButton.getPermissionCode());
        }
    }

    private void dealButton(AnEntity anEntity, AnnoTableButtonImpl tableButton) {
        String joinPermissionCode = joinPermission(anEntity.getAnnoPermission().getBaseCode(), tableButton.getPermissionCode());
        String joinPermissionName = joinPermission(anEntity.getName(), tableButton.getName());
        permissionCodeAndNameMap.put(joinPermissionCode, joinPermissionName);
    }

    private void dealButton(AnEntity anEntity, AnnoChartFieldImpl anChartField) {
        String joinPermissionCode = joinPermission(anEntity.getAnnoPermission().getBaseCode(), anChartField.getPermissionCode());
        String joinPermissionName = joinPermission(anEntity.getName(), anChartField.getName());
        permissionCodeAndNameMap.put(joinPermissionCode, joinPermissionName);
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
        return Arrays.stream(permissionList).collect(Collectors.joining(":"));
    }

    public String getPermissionKey(Class<?> javaCmdBeanClass, String javaCmdMethodName) {
        return getPermissionKey(javaCmdBeanClass.getName(), javaCmdMethodName);
    }

    public String getPermissionKey(String javaCmdBeanClassName, String javaCmdMethodName) {
        return javaCmdBeanClassName + "#" + javaCmdMethodName;
    }


}
