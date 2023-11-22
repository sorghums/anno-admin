package site.sorghum.anno.trans;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.plugin.join.aop.EasyJoin;
import site.sorghum.plugin.join.aop.JoinResMap;
import site.sorghum.plugin.join.aop.TransitionMap;
import site.sorghum.plugin.join.entity.JoinParam;
import site.sorghum.plugin.join.exception.JoinAnnoException;
import site.sorghum.plugin.join.operator.JoinOperator;
import site.sorghum.plugin.join.util.InvokeUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class AnnoTransService {

    @Inject
    JoinOperator joinOperator;

    @Inject
    MetadataManager metadataManager;

    /**
     * 批量翻译
     *
     * @param t t
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> trans(List<T> t, Class<?> tClass) {
        // 批量翻译
        List<JoinParam> joinParams = new ArrayList<>();
        AnEntity entity = metadataManager.getEntity(tClass);
        for (AnField field : entity.getFields()) {
            AnnoDataType dataType = field.getDataType();
            if (dataType == AnnoDataType.OPTIONS) {
                if (!Objects.equals(field.getOptionAnnoClass().getAnnoClass(), Object.class)) {
                    AnEntity optionClass = metadataManager.getEntity(field.getOptionAnnoClass().getAnnoClass());
                    String tableName = optionClass.getTableName();
                    String isLogicRemove = optionClass.getRemoveType() == 1 ? " and %s = %s".formatted(optionClass.getRemoveField(), optionClass.getNotRemoveValue()) : "";
                    String querySql = """
                        select %s as %s,%s from %s where %s = #{uniqueKey} %s
                        """.formatted(field.getOptionAnnoClass().getLabelKey()
                        , field.getFieldName() + "_label"
                        , field.getOptionAnnoClass().getIdKey()
                        , tableName
                        , field.getOptionAnnoClass().getIdKey()
                        , isLogicRemove);
                    joinParams.add(
                        new JoinParam(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , field.getOptionAnnoClass().getIdKey()
                            , null
                            , null
                            , querySql
                            , Map.class
                            , true
                            , new EasyJoin.RpData[]{}
                            , false
                            , null));
                }
                if (StrUtil.isNotBlank(field.getOptionTypeSql())) {
                    //select value, label from table where del_flag = 0 order by id desc
                    String originalSql = field.getOptionTypeSql();
                    String newSql = """
                        select label as %s,id as %s from ( %s ) temp where id = #{uniqueKey}
                         """.formatted(field.getFieldName() + "_label"
                        , field.getOptionAnnoClass().getIdKey(),
                        QuerySqlCache.get(originalSql));
                    joinParams.add(
                        new JoinParam(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , field.getOptionAnnoClass().getIdKey()
                            , null
                            , null
                            , newSql
                            , Map.class
                            , true
                            , new EasyJoin.RpData[]{}
                            , false
                            , null));
                }
                if (CollUtil.isNotEmpty(field.getOptionDatas())) {
                    Map<String, String> optionsMap = field.getOptionDatas().stream().collect(Collectors.toMap(AnField.OptionData::getValue, AnField.OptionData::getLabel));
                    fixedDictTrans(t, optionsMap, field.getFieldName());
                }
            }
            if (dataType == AnnoDataType.TREE) {
                if (!Objects.equals(field.getTreeOptionAnnoClass().getAnnoClass(), Object.class)) {
                    AnEntity optionClass = metadataManager.getEntity(field.getTreeOptionAnnoClass().getAnnoClass());
                    String tableName = optionClass.getTableName();
                    String isLogicRemove = optionClass.getRemoveType() == 1 ? " and %s = %s".formatted(optionClass.getRemoveField(), optionClass.getNotRemoveValue()) : "";
                    String querySql = """
                        select %s as %s,%s from %s where %s = #{uniqueKey} %s
                        """.formatted(field.getTreeOptionAnnoClass().getLabelKey()
                        , field.getFieldName() + "_label"
                        , field.getTreeOptionAnnoClass().getIdKey()
                        , tableName
                        , field.getTreeOptionAnnoClass().getIdKey()
                        , isLogicRemove);
                    joinParams.add(
                        new JoinParam(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , field.getOptionAnnoClass().getIdKey()
                            , null
                            , null
                            , querySql
                            , Map.class
                            , true
                            , new EasyJoin.RpData[]{}
                            , false
                            , null));
                }
                if (StrUtil.isNotBlank(field.getTreeTypeSql())) {
                    //select value, label from table where del_flag = 0 order by id desc
                    String originalSql = field.getTreeTypeSql();
                    String newSql = """
                        select label as %s,id as %s from ( %s ) temp where id = #{uniqueKey}
                         """.formatted(field.getFieldName() + "_label"
                        , field.getOptionAnnoClass().getIdKey(),
                        QuerySqlCache.get(originalSql));
                    joinParams.add(
                        new JoinParam(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , field.getOptionAnnoClass().getIdKey()
                            , null
                            , null
                            , newSql
                            , Map.class
                            , true
                            , new EasyJoin.RpData[]{}
                            , false
                            , null));
                }
                if (CollUtil.isNotEmpty(field.getTreeDatas())) {
                    Map<String, String> optionsMap = field.getOptionDatas().stream().collect(Collectors.toMap(AnField.OptionData::getValue, AnField.OptionData::getLabel));
                    fixedDictTrans(t, optionsMap, field.getFieldName());
                }
            }
        }
        for (JoinParam joinParam : joinParams) {
            joinOperator.batchJoinOne(joinParam, t);
        }
        return t;
    }

    private <T> void fixedDictTrans(List<T> t, Map<String, String> dict, String fieldName) {
        for (T tItem : t) {
            Object fieldValue = ReflectUtil.getFieldValue(tItem, fieldName);
            if (fieldValue == null) continue;
            Map<Object, Object> tansMap = JoinParam.getJoinResMap(tItem);
            tansMap.put(fieldName + "_label", dict.getOrDefault(String.valueOf(fieldValue), String.valueOf(fieldValue)));
        }
    }

}
