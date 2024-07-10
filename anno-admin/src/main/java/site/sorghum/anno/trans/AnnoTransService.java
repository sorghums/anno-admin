package site.sorghum.anno.trans;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemoveImpl;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeTypeImpl;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.tree.TreeDataSupplier;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.QuerySqlCache;
import site.sorghum.anno.anno.util.ReentrantStopWatch;
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

    public static List<AnnoDataType> OPTIONS_TYPE = Arrays.asList(AnnoDataType.OPTIONS, AnnoDataType.PICKER, AnnoDataType.RADIO, AnnoDataType.CLASS_OPTIONS);

    /**
     * 批量翻译
     *
     * @param t t
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> trans(List<T> t, Class<?> tClass) {
        ReentrantStopWatch stopWatch = AnnoContextUtil.getContext().getStopWatch();
        stopWatch.start("trans from db/cache");
        // 批量翻译
        List<JoinParam> joinParams = new ArrayList<>();
        AnEntity entity = metadataManager.getEntity(tClass);
        for (AnField field : entity.getFields()) {
            AnnoDataType dataType = field.getDataType();
            if (OPTIONS_TYPE.contains(dataType)) {
                AnnoOptionTypeImpl optionType = field.getOptionType();
                AnnoOptionTypeImpl.OptionAnnoClassImpl optionAnno = optionType.getOptionAnno();
                String sqlIdKey = fileNameToTableName(optionAnno.getAnnoClass(), optionAnno.getIdKey());
                String sqlLabelKey = fileNameToTableName(optionAnno.getAnnoClass(), optionAnno.getLabelKey());
                if (!Objects.equals(optionAnno.getAnnoClass(), Object.class)) {
                    AnEntity optionClass = metadataManager.getEntity(optionAnno.getAnnoClass());
                    String tableName = optionClass.getTableName();
                    AnnoRemoveImpl annoRemove = optionClass.getAnnoRemove();
                    String isLogicRemove = annoRemove.getRemoveType() == 1 ? " and %s = %s".formatted(annoRemove.getRemoveField(), annoRemove.getNotRemoveValue()) : "";
                    String querySql = """
                        select %s as %s,%s from %s where %s = #{uniqueKey} %s
                        """.formatted(sqlLabelKey
                        , field.getJavaName().toLowerCase() + "_label"
                        , sqlIdKey
                        , tableName
                        , sqlIdKey
                        , isLogicRemove);
                    joinParams.add(
                        new JoinParam<>(field.getJavaName()
                            , field.getJavaName()
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
                String optionSql = field.getOptionType().getSqlKey();
                if (StrUtil.isNotBlank(optionSql)) {
                    //select value, label from table where del_flag = 0 order by id desc
                    String newSql = """
                        select label as %s,id as %s from ( %s ) temp where id = #{uniqueKey}
                         """.formatted(field.getJavaName().toLowerCase() + "_label"
                        , sqlIdKey,
                        QuerySqlCache.get(optionSql));
                    joinParams.add(
                        new JoinParam<>(field.getJavaName()
                            , field.getJavaName()
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
                List<AnnoOptionTypeImpl.OptionDataImpl> optionData = Arrays.asList(field.getOptionType().getValue());
                if (CollUtil.isNotEmpty(optionData)) {
                    Map<String, String> optionsMap = optionData.stream().collect(Collectors.toMap(AnnoOptionTypeImpl.OptionDataImpl::getValue, AnnoOptionTypeImpl.OptionDataImpl::getLabel));
                    fixedDictTrans(t, optionsMap, field.getJavaName());
                }
                Class<? extends OptionDataSupplier> optionSupplier = field.getOptionType().getSupplier();
                if (optionSupplier != null && optionSupplier != OptionDataSupplier.class) {
                    List<AnnoOptionTypeImpl.OptionDataImpl> optionDataList = AnnoBeanUtils.getBean(optionSupplier).getOptionDataList();
                    Map<String, String> optionsMap = optionDataList.stream().collect(Collectors.toMap(AnnoOptionTypeImpl.OptionDataImpl::getValue, AnnoOptionTypeImpl.OptionDataImpl::getLabel));
                    fixedDictTrans(t, optionsMap, field.getJavaName());
                }
            }
            if (dataType == AnnoDataType.TREE) {
                AnnoTreeTypeImpl.TreeAnnoClassImpl treeAnnoClass = field.getTreeType().getTreeAnno();
                String sqlIdKey = fileNameToTableName(treeAnnoClass.getAnnoClass(), treeAnnoClass.getIdKey());
                String sqlLabelKey = fileNameToTableName(treeAnnoClass.getAnnoClass(), treeAnnoClass.getLabelKey());
                if (!Objects.equals(treeAnnoClass.getAnnoClass(), Object.class)) {
                    AnEntity optionClass = metadataManager.getEntity(treeAnnoClass.getAnnoClass());
                    String tableName = optionClass.getTableName();
                    AnnoRemoveImpl optionClassAnnoRemove = optionClass.getAnnoRemove();
                    String isLogicRemove = optionClassAnnoRemove.getRemoveType() == 1 ? " and %s = %s".formatted(optionClassAnnoRemove.getRemoveField(), optionClassAnnoRemove.getNotRemoveValue()) : "";
                    String querySql = """
                        select %s as %s,%s from %s where %s = #{uniqueKey} %s
                        """.formatted(sqlLabelKey
                        , field.getJavaName().toLowerCase() + "_label"
                        , sqlIdKey
                        , tableName
                        , sqlIdKey
                        , isLogicRemove);
                    joinParams.add(
                        new JoinParam(field.getJavaName()
                            , field.getJavaName()
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
                String treeTypeSql = field.getTreeType().getSqlKey();
                if (StrUtil.isNotBlank(treeTypeSql)) {
                    //select value, label from table where del_flag = 0 order by id desc
                    String newSql = """
                        select label as %s,id as %s from ( %s ) temp where id = #{uniqueKey}
                         """.formatted(field.getJavaName().toLowerCase() + "_label"
                        , sqlIdKey,
                        QuerySqlCache.get(treeTypeSql));
                    joinParams.add(
                        new JoinParam(field.getJavaName()
                            , field.getJavaName()
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
                List<AnnoTreeType.TreeData> treeData = Arrays.asList(field.getTreeType().getValue());
                if (CollUtil.isNotEmpty(treeData)) {
                    Map<String, String> optionsMap = treeData.stream().collect(Collectors.toMap(AnnoTreeType.TreeData::id, AnnoTreeType.TreeData::label));
                    fixedDictTrans(t, optionsMap, field.getJavaName());
                }
                Class<? extends TreeDataSupplier> treeSupplier = field.getTreeType().getSupplier();
                if (treeSupplier != null && treeSupplier != TreeDataSupplier.class) {
                    List<AnnoTreeTypeImpl.TreeDataImpl> treeDataList = AnnoBeanUtils.getBean(treeSupplier).getTreeDataList();
                    Map<String, String> optionsMap = treeDataList.stream().collect(Collectors.toMap(AnnoTreeTypeImpl.TreeDataImpl::getId, AnnoTreeTypeImpl.TreeDataImpl::getLabel));
                    fixedDictTrans(t, optionsMap, field.getJavaName());
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
        return t;
    }

    private <T> void fixedDictTrans(List<T> t, Map<String, String> dict, String fieldName) {
        for (T tItem : t) {
            Object fieldValue = ReflectUtil.getFieldValue(tItem, fieldName);
            if (fieldValue == null) continue;
            Map<Object, Object> tansMap = JoinParam.getJoinResMap(tItem);
            List<String> fieldValues = parseStr2Array(String.valueOf(fieldValue));
            StringBuilder finalResult = new StringBuilder();
            for (String value : fieldValues) {
                String dictOrDefault = dict.getOrDefault(value, String.valueOf(fieldValue));
                finalResult.append(dictOrDefault).append(",");
            }
            if (!finalResult.isEmpty()) {
                finalResult.setLength(finalResult.length() - 1);
            }
            tansMap.put(fieldName.toLowerCase() + "_label", finalResult);
        }
    }

    private List<String> parseStr2Array(String value) {
        List<String> valueList = JSONUtil.toBeanList(value, String.class);
        if (valueList.isEmpty()) {
            return List.of(value);
        }
        return valueList;
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
