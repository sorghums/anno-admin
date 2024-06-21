package site.sorghum.anno._metadata;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno.anno.annotation.clazz.AnnoForm;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoMany2ManyField;
import site.sorghum.anno.anno.annotation.field.type.AnnoFileType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.entity.common.FieldAnnoField;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.tpl.BaseTplRender;
import site.sorghum.anno.anno.tree.TreeDataSupplier;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * anno 实体 元数据加载
 *
 * @author songyinyin
 * @since 2023/7/15 19:21
 */
@Named
public class EntityMetadataLoader implements MetadataLoader<Class<?>> {

    @Override
    public String getEntityName(Class<?> entity) {
        // 如果是匿名类,取实际类
        while (entity.isAnonymousClass()) {
            entity = entity.getSuperclass();
        }
        return entity.getSimpleName();
    }

    /**
     * 加载AnEntity对象
     *
     * @param clazz 类对象
     * @return AnEntity对象
     */
    @Override
    public AnEntity load(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        Table table = clazz.getAnnotation(Table.class);
        AnEntity entity = new AnEntity();
        entity.setTitle(annoMain.name());
        entity.setCanRemove(annoMain.canRemove());
        if (table == null || StrUtil.isBlank(table.value())) {
            if (StrUtil.isNotBlank(annoMain.tableName())) {
                entity.setTableName(annoMain.tableName());
            } else {
                entity.setTableName(StrUtil.toUnderlineCase(getEntityName(clazz)));
            }
        } else {
            entity.setTableName(table.value());
        }
        entity.setAutoMaintainTable(annoMain.autoMaintainTable());
        entity.setOrgFilter(annoMain.orgFilter());
        entity.setVirtualTable(annoMain.virtualTable());
        if (entity.isVirtualTable()) {
            // 虚拟表不需要维护
            entity.setAutoMaintainTable(false);
        }

        entity.setClazz(clazz);
        entity.setEntityName(getEntityName(clazz));

        List<AnOrder> anOrders = Arrays.stream(annoMain.annoOrder()).map(
            annoOrder -> new AnOrder(annoOrder.orderType(), annoOrder.orderValue())
        ).toList();
        entity.setAnOrder(anOrders);

        // 权限
        entity.setEnablePermission(annoMain.annoPermission().enable());
        entity.setPermissionCode(annoMain.annoPermission().baseCode());
        entity.setPermissionCodeTranslate(annoMain.annoPermission().baseCodeTranslate());

        // 树
        entity.setEnableLeftTree(annoMain.annoLeftTree().enable());
        entity.setLeftTreeName(annoMain.annoLeftTree().leftTreeName());
        entity.setLeftTreeCatKey(annoMain.annoLeftTree().catKey());
        entity.setLeftTreeClass(annoMain.annoLeftTree().treeClass());

        // Anno树
        entity.setEnableTree(annoMain.annoTree().enable());
        entity.setTreeLabel(annoMain.annoTree().label());
        entity.setTreeParentKey(annoMain.annoTree().parentKey());
        entity.setTreeKey(annoMain.annoTree().key());
        entity.setTreeDisplayAsTree(annoMain.annoTree().displayAsTree());

        // 逻辑删除
        AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
        entity.setRemoveType(annoRemove.removeType());
        entity.setRemoveField(annoRemove.removeField());
        entity.setRemoveValue(annoRemove.removeValue());
        entity.setNotRemoveValue(annoRemove.notRemoveValue());

        // 类字段
        setAnFields(entity, clazz);

        // 多对多字段
        setAnMany2ManyFields(entity, clazz);

        // 行级按钮
        List<AnColumnButton> anColumnButtons = getAnButton(clazz);
        entity.setColumnButtons(anColumnButtons);

        // 表级按钮
        List<AnButton> anTableButtons = getAnTableButton(clazz);
        entity.setTableButtons(anTableButtons);

        // 加载图表
        loadChart(annoMain, entity);
        return entity;
    }

    /**
     * 加载AnEntity对象
     *
     * @param clazz 类对象
     * @return AnEntity对象
     */
    @Override
    public AnEntity loadForm(Class<?> clazz) {
        AnnoForm annoForm = AnnoUtil.getAnnoForm(clazz);
        AnEntity entity = new AnEntity();
        entity.setTitle(annoForm.name());
        entity.setVirtualTable(true);
        entity.setAutoMaintainTable(false);
        entity.setClazz(clazz);
        entity.setEntityName(getEntityName(clazz));
        // 类字段
        setAnFields(entity, clazz);
        // 多对多字段
        setAnMany2ManyFields(entity, clazz);
        return entity;
    }

    /**
     * 加载图表
     *
     * @param annoMain AnnoMain对象
     * @param entity AnEntity对象
     */
    private void loadChart(AnnoMain annoMain, AnEntity entity) {
        entity.setAnChart(
            new AnChart(annoMain.annoChart())
        );
    }


    /**
     * 设置字段信息和主键字段
     *
     * @param entity AnEntity对象
     * @param clazz  需要设置字段信息的类
     */
    private void setAnFields(AnEntity entity, Class<?> clazz) {
        List<FieldAnnoField> fields = AnnoUtil.getAnnoFields(clazz);
        List<AnField> anFields = new ArrayList<>();
        boolean virtualTable = entity.isVirtualTable();
        for (FieldAnnoField fieldAnnoField : fields) {
            AnnoField anno = fieldAnnoField.getAnnoField();
            Field field = fieldAnnoField.getField();
            AnField anField = new AnField();
            anField.setFieldName(field.getName());
            anField.setReflectField(field);
            anField.setDeclaringClass(field.getDeclaringClass());
            anField.setVirtualColumn(anno.virtualColumn());
            anField.setTitle(anno.title());
            // 列名没有设置时，默认使用下划线
            if (StrUtil.isBlank(anno.tableFieldName())) {
                if (virtualTable || anno.virtualColumn()) {
                    anField.setTableFieldName(field.getName());
                } else {
                    anField.setTableFieldName(StrUtil.toUnderlineCase(field.getName()));
                }
            } else {
                anField.setTableFieldName(anno.tableFieldName());
            }

            anField.setFieldType(field.getType());
            anField.setFieldSize(anno.fieldSize());
            anField.setShow(anno.show());

            anField.setSearchEnable(anno.search().enable());
            anField.setSearchNotNull(anno.search().notNull());
            anField.setSearchQueryType(anno.search().queryType());
            anField.setSearchPlaceHolder(anno.search().placeHolder());

            anField.setAddEnable(anno.edit().addEnable());
            anField.setEditEnable(anno.edit().editEnable());
            anField.setEditCanClear(anno.edit().canClear());
            anField.setEditNotNull(anno.edit().notNull());
            anField.setEditPlaceHolder(anno.edit().placeHolder());
            anField.setEditSpan(anno.edit().span());
            AnnoEdit.ShowBy showBy = anno.edit().showBy();
            AnField.EditShowBy editShowBy = new AnField.EditShowBy();
            editShowBy.setEnable(showBy.enable());
            editShowBy.setExpr(showBy.expr());
            anField.setEditShowBy(editShowBy);

            anField.setDataType(anno.dataType());

            anField.setOptionIsMultiple(anno.optionType().isMultiple());
            if (StrUtil.isNotBlank(anno.optionType().sql())) {
                String optionQuerySqlCacheKey = QuerySqlCache.generateKey(anField.getFieldName(), anno.optionType().sql());
                QuerySqlCache.put(optionQuerySqlCacheKey, anno.optionType().sql());
                anField.setOptionTypeSql(optionQuerySqlCacheKey);
            }
            AnnoOptionType.OptionAnnoClass optionAnnoClass = anno.optionType().optionAnno();
            anField.setOptionAnnoClass(
                new AnField.OptionAnnoClass(optionAnnoClass.labelKey(), optionAnnoClass.idKey(), optionAnnoClass.annoClass())
            );
            anField.setOptionEnum(anno.optionType().optionEnum());
            AnnoOptionType.OptionData[] optionData = anno.optionType().value();
            Class<? extends OptionDataSupplier> optionSupplier = anno.optionType().supplier();
            List<AnField.OptionData> optionDataList;
            if (anField.getOptionEnum() != Enum.class) {
                optionDataList = AnnoUtil.enum2OptionData(anField.getOptionEnum());
            } else if (optionData.length > 0) {
                optionDataList = Arrays.stream(optionData)
                    .map(e -> new AnField.OptionData(e.label(), e.value()))
                    .collect(Collectors.toList());
            }else {
                optionDataList = Collections.emptyList();
            }
            anField.setOptionDatas(optionDataList);
            if (optionSupplier != OptionDataSupplier.class){
                anField.setOptionSupplier(optionSupplier);
            }

            // 图像
            anField.setImageEnlargeAble(anno.imageType().enlargeAble());
            anField.setImageWidth(anno.imageType().width());
            anField.setImageHeight(anno.imageType().height());

            // 选择类型-树
            if (StrUtil.isNotBlank(anno.treeType().sql())) {
                String treeQuerySqlCacheKey = QuerySqlCache.generateKey(anField.getFieldName(), anno.treeType().sql());
                QuerySqlCache.put(treeQuerySqlCacheKey, anno.treeType().sql());
                anField.setTreeTypeSql(treeQuerySqlCacheKey);
            }
            AnnoTreeType.TreeAnnoClass treeAnnoClass = anno.treeType().treeAnno();
            Class<? extends TreeDataSupplier> treeOptionSupplier = anno.treeType().supplier();
            anField.setTreeOptionAnnoClass(
                new AnField.TreeAnnoClass(treeAnnoClass.annoClass(), treeAnnoClass.idKey(), treeAnnoClass.labelKey(), treeAnnoClass.pidKey())
            );
            if (treeOptionSupplier != TreeDataSupplier.class){
                anField.setTreeOptionSupplier(treeOptionSupplier);
            }
            AnnoTreeType.TreeData[] treeData = anno.treeType().value();
            List<AnField.TreeData> treeDataList = Arrays.stream(treeData)
                .map(e -> new AnField.TreeData(e.id(), e.label(), e.pid()))
                .collect(Collectors.toList());
            anField.setTreeDatas(treeDataList);
            anField.setTreeOptionIsMultiple(anno.treeType().isMultiple());

            // 文件类型
            AnnoFileType annoFileType = anno.fileType();
            anField.setFileType(annoFileType.fileType());
            anField.setFileMaxCount(annoFileType.fileMaxCount());
            anField.setFileMaxSize(annoFileType.fileMaxSize());

            // pk
            PrimaryKey primaryKey = fieldAnnoField.getPrimaryKey();
            if (primaryKey != null) {
                anField.setPrimaryKey(true);
                entity.setPkField(anField);
            }

            // 排序
            anField.setSort(anno.sort());

            // 字段设置
            anField.setInsertWhenNullSet(
                anno.insertWhenNullSet()
            );
            anField.setUpdateWhenNullSet(
                anno.updateWhenNullSet()
            );

            anFields.add(anField);
        }
        anFields.sort(Comparator.comparingInt(AnField::getSort).reversed());
        entity.setFields(anFields);
    }

    /**
     * 设置AnEntity对象的多对多字段
     *
     * @param entity AnEntity对象
     * @param clazz  需要设置多对多字段的类
     */
    private void setAnMany2ManyFields(AnEntity entity, Class<?> clazz) {
        List<Field> fields = AnnoUtil.getAnnoMany2ManyFields(clazz);
        List<AnMany2ManyField> annoMany2ManyFields = new ArrayList<>();
        for (Field field : fields) {
            AnnoMany2ManyField anno = AnnotationUtil.getAnnotation(field, AnnoMany2ManyField.class);
            AnMany2ManyField anMany2ManyField = parseAnMany2ManyField(field, anno);

            annoMany2ManyFields.add(anMany2ManyField);
        }
        entity.setMany2ManyFields(annoMany2ManyFields);
    }

    /**
     * 根据给定的Field和AnnoMany2ManyField注解获取AnMany2ManyField对象
     *
     * @param field 给定的Field对象
     * @param anno 给定的AnnoMany2ManyField注解对象
     * @return 返回一个AnMany2ManyField对象
     */
    private static AnMany2ManyField parseAnMany2ManyField(Field field, AnnoMany2ManyField anno) {
        AnMany2ManyField anMany2ManyField = new AnMany2ManyField();
        anMany2ManyField.setField(field);
        anMany2ManyField.setMediumTable(anno.mediumTable());

        anMany2ManyField.setOtherColumnMediumName(anno.otherColumn().mediumName());
        anMany2ManyField.setOtherColumnReferencedName(anno.otherColumn().referencedName());

        anMany2ManyField.setThisColumnMediumName(anno.thisColumn().mediumName());
        anMany2ManyField.setThisColumnReferencedName(anno.thisColumn().referencedName());
        return anMany2ManyField;
    }

    private List<AnColumnButton> getAnButton(Class<?> clazz) {
        ArrayList<AnColumnButton> anColumnButtons = new ArrayList<>();

        List<Field> annoButtonFields = AnnoUtil.getAnnoButtonFields(clazz);
        for (Field buttonField : annoButtonFields) {
            AnnoButton anno = AnnotationUtil.getAnnotation(buttonField, AnnoButton.class);
            AnColumnButton anColumnButton = new AnColumnButton();
            anColumnButton.setName(anno.name());
            anColumnButton.setIcon(anno.icon());
            anColumnButton.setPermissionCode(anno.permissionCode());
            anColumnButton.setFormEntity(AnnoBeanUtils.getBean(anno.baseForm()).getEntity());
            anColumnButton.setSize(anno.size());
            anColumnButton.setJsCmd(anno.jsCmd());
            anColumnButton.setJumpUrl(anno.jumpUrl());

            // 一对多
            anColumnButton.setO2mEnable(anno.o2mJoinButton().enable());
            anColumnButton.setO2mTargetClass(anno.o2mJoinButton().targetClass());
            anColumnButton.setO2mThisJavaField(anno.o2mJoinButton().thisJavaField());
            anColumnButton.setO2mTargetJavaField(anno.o2mJoinButton().targetJavaField());

            // 多对多
            anColumnButton.setM2mEnable(anno.m2mJoinButton().enable());
            if (anColumnButton.getM2mEnable()) {
                AnnoMtm annoMtm = new AnnoMtm();
                annoMtm.setM2mJoinTargetClazz(getEntityName(anno.m2mJoinButton().joinTargetClazz()));
                annoMtm.setM2mJoinThisClazz(getEntityName(clazz));
                annoMtm.setM2mMediumTableClass(getEntityName(anno.m2mJoinButton().mediumTableClass()));
                annoMtm.setM2mMediumThisField(anno.m2mJoinButton().mediumThisField());
                annoMtm.setM2mMediumTargetField(anno.m2mJoinButton().mediumTargetField());
                annoMtm.setM2mJoinThisClazzField(anno.m2mJoinButton().joinThisClazzField());
                annoMtm.setM2mJoinTargetClazzField(anno.m2mJoinButton().joinTargetClazzField());
                annoMtm.setId(annoMtm.getM2mJoinThisClazz() + "::" + MD5Util.digestHex(JSONUtil.toJsonString(annoMtm)));
                AnnoMtm.annoMtmMap.put(annoMtm.getId(), annoMtm);
                anColumnButton.setM2mData(annoMtm);
            }

            // java cmd
            anColumnButton.setJavaCmdEnable(anno.javaCmd().enable());
            if (anColumnButton.getJavaCmdEnable()) {
                AnnoJavaCmd annoJavaCmd = new AnnoJavaCmd();
                annoJavaCmd.setRunSupplier(anno.javaCmd().runSupplier());
                annoJavaCmd.setPermissionCode(anno.permissionCode());
                annoJavaCmd.setId(getEntityName(clazz) + "::" + MD5Util.digestHex(JSONUtil.toJsonString(annoJavaCmd)));
                AnnoJavaCmd.annoJavCmdMap.put(annoJavaCmd.getId(), annoJavaCmd);
                anColumnButton.setJavaCmdData(annoJavaCmd);
            }

            // tpl
            anColumnButton.setTplId(BaseTplRender.toId(anno.annoTpl().tplClazz()));
            anColumnButton.setTplName(anno.name());
            anColumnButton.setTplEnable(anno.annoTpl().enable());
            anColumnButton.setTplWindowWidth(anno.annoTpl().windowWidth());
            anColumnButton.setTplWindowHeight(anno.annoTpl().windowHeight());
            anColumnButton.setTplClazz(anno.annoTpl().tplClazz());


            anColumnButtons.add(anColumnButton);
        }
        return anColumnButtons;
    }

    private List<AnButton> getAnTableButton(Class<?> clazz) {
        ArrayList<AnButton> anButtons = new ArrayList<>();

        AnnoMain main = AnnoUtil.getAnnoMain(clazz);
        AnnoTableButton[] annoTableButtons = main.annoTableButton();
        for (AnnoTableButton anno : annoTableButtons) {

            AnButton anButton = new AnButton();
            anButton.setName(anno.name());
            anButton.setIcon(anno.icon());
            anButton.setPermissionCode(anno.permissionCode());
            anButton.setSize(anno.size());
            anButton.setFormEntity(AnnoBeanUtils.getBean(anno.baseForm()).getEntity());
            anButton.setJsCmd(anno.jsCmd());
            anButton.setJumpUrl(anno.jumpUrl());

            // java cmd
            anButton.setJavaCmdEnable(anno.javaCmd().enable());
            if (anButton.getJavaCmdEnable()) {
                AnnoJavaCmd annoJavaCmd = new AnnoJavaCmd();
                annoJavaCmd.setRunSupplier(anno.javaCmd().runSupplier());
                annoJavaCmd.setPermissionCode(anno.permissionCode());
                annoJavaCmd.setId(getEntityName(clazz) + "::" + MD5Util.digestHex(JSONUtil.toJsonString(annoJavaCmd)));
                AnnoJavaCmd.annoJavCmdMap.put(annoJavaCmd.getId(), annoJavaCmd);
                anButton.setJavaCmdData(annoJavaCmd);
            }
            anButtons.add(anButton);
        }
        return anButtons;
    }


}
