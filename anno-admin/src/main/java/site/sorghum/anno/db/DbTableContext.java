package site.sorghum.anno.db;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.EntityMetadataLoader;
import site.sorghum.anno._metadata.MetadataContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Anno Clazz 缓存
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Named
public class DbTableContext implements MetadataContext {

    @Inject
    private EntityMetadataLoader entityMetadataLoader;

    /**
     * Anno Clazz 缓存
     */
    private final Map<String, TableParam<?>> tableParamCache = new HashMap<>();

    /**
     * 获取缓存
     *
     * @param entityName 对象名
     * @return {@link Class}<{@link ?}>
     */
    public <T> TableParam<T> getTableParam(String entityName) {
        TableParam<?> tableParam = tableParamCache.get(entityName);
        // 复制一份，防止被修改
        TableParam<T> returnParam = new TableParam<>();
        returnParam.setTableName(tableParam.getTableName());
        returnParam.setClazz(tableParam.getClazz());
        returnParam.setColumns(new ArrayList<>(tableParam.getColumns()));
        returnParam.setDbRemove(tableParam.getDbRemove());
        returnParam.setVirtualTable(tableParam.isVirtualTable());
        returnParam.setJoinTables(new ArrayList<>(tableParam.getJoinTables()));
        return returnParam;
    }

    /**
     * 获取缓存（不可修改）
     *
     * @param entityName 实体名称
     */
    public <T> TableParam<T> getTableParamImmutable(String entityName) {
        return (TableParam<T>) tableParamCache.get(entityName);
    }

    /**
     * 获取 entity db 数据
     *
     * @param clazz 实体类
     */
    public <T> TableParam<T> getTableParam(Class<?> clazz) {
        String entityName = entityMetadataLoader.getEntityName(clazz);
        return getTableParam(entityName);
    }

    @Override
    public void refresh(List<AnEntity> allEntities) {
        tableParamCache.clear();

        for (AnEntity entity : allEntities) {
            TableParam<?> tableParam = new TableParam<>();

            tableParam.setClazz(entity.getClazz());
            tableParam.setTableName(entity.getTableName());
            List<String> columns = entity.getDbAnFields().stream().map(AnField::getTableFieldName).collect(Collectors.toList());
            tableParam.setColumns(columns);
            if (entity.getRemoveType() == 0) {
                tableParam.setDbRemove(new DbRemove());
            } else {
                tableParam.setDbRemove(new DbRemove(true, entity.getRemoveField(), entity.getRemoveValue(), entity.getNotRemoveValue()));
            }
            // 设置是否虚拟表
            tableParam.setVirtualTable(entity.isVirtualTable());
            // 设置连表信息
            if (entity.getJoinTable() != null) {
                String mainTableName = entity.getJoinTable().getMainTable();
                if (StrUtil.isNotBlank(entity.getJoinTable().getMainAlias())) {
                    mainTableName = entity.getJoinTable().getMainTable() + " as " + entity.getJoinTable().getMainAlias();
                }
                tableParam.setTableName(mainTableName);
                List<TableParam.JoinTable> joinTables = entity.getJoinTable().getJoinTables().stream()
                    .map(joinTable -> new TableParam.JoinTable(joinTable.getTable(), joinTable.getAlias(), joinTable.getJoinType(), joinTable.getJoinCondition()))
                    .toList();
                tableParam.setJoinTables(joinTables);
            }
            tableParamCache.put(entity.getEntityName(), tableParam);
        }
    }
}
