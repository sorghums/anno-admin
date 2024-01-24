package site.sorghum.anno.trans;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.plugin.join.aop.EasyJoin;
import site.sorghum.plugin.join.entity.JoinParam;
import site.sorghum.plugin.join.operator.JoinOperator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sorghum
 */
@Named
@Slf4j
public class AnnoTransService {

    @Inject
    JoinOperator joinOperator;

    @Inject
    MetadataManager metadataManager;

    public static List<AnnoDataType> OPTIONS_TYPE = Arrays.asList(AnnoDataType.OPTIONS, AnnoDataType.PICKER, AnnoDataType.RADIO);

    /**
     * 批量翻译
     *
     * @param t t
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> trans(List<T> t, Class<?> tClass) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("trans from db/cache");
        // 批量翻译
        List<JoinParam> joinParams = new ArrayList<>();
        AnEntity entity = metadataManager.getEntity(tClass);
        for (AnField field : entity.getFields()) {
            AnnoDataType dataType = field.getDataType();
            if (OPTIONS_TYPE.contains(dataType)) {
                String sqlIdKey = fileNameToTableName(field.getOptionAnnoClass().getAnnoClass(), field.getOptionAnnoClass().getIdKey());
                String sqlLabelKey = fileNameToTableName(field.getOptionAnnoClass().getAnnoClass(), field.getOptionAnnoClass().getLabelKey());
                if (!Objects.equals(field.getOptionAnnoClass().getAnnoClass(), Object.class)) {
                    AnEntity optionClass = metadataManager.getEntity(field.getOptionAnnoClass().getAnnoClass());
                    String tableName = optionClass.getTableName();
                    String isLogicRemove = optionClass.getRemoveType() == 1 ? " and %s = %s".formatted(optionClass.getRemoveField(), optionClass.getNotRemoveValue()) : "";
                    String querySql = """
                        select %s as %s,%s from %s where %s = #{uniqueKey} %s
                        """.formatted(sqlLabelKey
                        , field.getFieldName().toLowerCase() + "_label"
                        , sqlIdKey
                        , tableName
                        , sqlIdKey
                        , isLogicRemove);
                    joinParams.add(
                        new JoinParam<>(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , sqlIdKey
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
                         """.formatted(field.getFieldName().toLowerCase() + "_label"
                        , sqlIdKey,
                        QuerySqlCache.get(originalSql));
                    joinParams.add(
                        new JoinParam<>(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , sqlIdKey
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
                String sqlIdKey = fileNameToTableName(field.getTreeOptionAnnoClass().getAnnoClass(), field.getTreeOptionAnnoClass().getIdKey());
                String sqlLabelKey = fileNameToTableName(field.getTreeOptionAnnoClass().getAnnoClass(), field.getTreeOptionAnnoClass().getLabelKey());
                if (!Objects.equals(field.getTreeOptionAnnoClass().getAnnoClass(), Object.class)) {
                    AnEntity optionClass = metadataManager.getEntity(field.getTreeOptionAnnoClass().getAnnoClass());
                    String tableName = optionClass.getTableName();
                    String isLogicRemove = optionClass.getRemoveType() == 1 ? " and %s = %s".formatted(optionClass.getRemoveField(), optionClass.getNotRemoveValue()) : "";
                    String querySql = """
                        select %s as %s,%s from %s where %s = #{uniqueKey} %s
                        """.formatted(sqlLabelKey
                        , field.getFieldName().toLowerCase() + "_label"
                        , sqlIdKey
                        , tableName
                        , sqlIdKey
                        , isLogicRemove);
                    joinParams.add(
                        new JoinParam(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , sqlIdKey
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
                         """.formatted(field.getFieldName().toLowerCase() + "_label"
                        , sqlIdKey,
                        QuerySqlCache.get(originalSql));
                    joinParams.add(
                        new JoinParam(field.getFieldName()
                            , field.getFieldName()
                            , null
                            , sqlIdKey
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
        // 所有列的JoinResMap的Key全部变成小写 to 兼容部分大小写不敏感的数据库
        stopWatch.stop();
        stopWatch.start("fixedDictToLowerCase");
        fixedDictToLowerCase(t);
        stopWatch.stop();
        log.info(
            "转换耗时：{} ms",
            stopWatch.getTotalTimeMillis()
        );
        return t;
    }

    private <T> void fixedDictTrans(List<T> t, Map<String, String> dict, String fieldName) {
        for (T tItem : t) {
            Object fieldValue = ReflectUtil.getFieldValue(tItem, fieldName);
            if (fieldValue == null) continue;
            Map<Object, Object> tansMap = JoinParam.getJoinResMap(tItem);
            tansMap.put(fieldName.toLowerCase() + "_label", dict.getOrDefault(String.valueOf(fieldValue), String.valueOf(fieldValue)));
        }
    }

    private <T> void fixedDictToLowerCase(List<T> t) {
        t.forEach(
            tItem -> {
                Map<Object, Object> tansMap = JoinParam.getJoinResMap(tItem);
                if (tansMap == null || tansMap.isEmpty()) {
                    return;
                }
                HashMap<Object, Object> newMap = new HashMap<>();
                tansMap.keySet().forEach(
                    (k) -> {
                        // 转换成小写
                            newMap.put(k.toString().toLowerCase(), tansMap.get(k));
                    }
                );
                tansMap.putAll(newMap);
            }
        );
    }

    private String fileNameToTableName(Class<?> clazz, String fileName) {
        if (clazz == Object.class || StrUtil.isBlank(fileName)) {
            return fileName;
        }
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(clazz, fileName);
        } catch (NullPointerException e) {
            log.error("从Java字段名映射Sql字段名出错,类：{}，字段：{}，错误信息：{}", clazz, fileName, e.getMessage());
            return fileName;
        }
    }

}
