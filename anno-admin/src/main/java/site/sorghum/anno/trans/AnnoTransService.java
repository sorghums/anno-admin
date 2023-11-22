package site.sorghum.anno.trans;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.plugin.join.aop.EasyJoin;
import site.sorghum.plugin.join.entity.JoinParam;
import site.sorghum.plugin.join.operator.JoinOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            }
        }
        for (JoinParam joinParam : joinParams) {
            joinOperator.batchJoinOne(joinParam, t);
        }
        return t;
    }
}
