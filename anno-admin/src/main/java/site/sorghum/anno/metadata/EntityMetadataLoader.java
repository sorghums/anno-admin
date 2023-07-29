package site.sorghum.anno.metadata;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * anno 实体 元数据加载
 *
 * @author songyinyin
 * @since 2023/7/15 19:21
 */
@Component
public class EntityMetadataLoader implements MetadataLoader<Class<?>> {

    @Override
    public String getEntityName(Class<?> entity) {
        return entity.getSimpleName();
    }

    @Override
    public AnEntity load(Class<?> clazz) {
        AnnoMain annoMain = clazz.getAnnotation(AnnoMain.class);
        Table table = clazz.getAnnotation(Table.class);

        AnEntity entity = new AnEntity();
        entity.setTitle(annoMain.name());
        if (table == null || StrUtil.isBlank(table.value())) {
            entity.setTableName(StrUtil.toUnderlineCase(getEntityName(clazz)));
        } else {
            entity.setTableName(table.value());
        }
        entity.setClazz(clazz);
        entity.setEntityName(getEntityName(clazz));

        entity.setOrderType(annoMain.annoOrder().orderType());
        entity.setOrderValue(annoMain.annoOrder().orderValue());

        entity.setPreProxy(AnnoUtil.getAnnoPreProxy(clazz).value());
        entity.setProxy(annoMain.annoProxy().value());

        entity.setEnablePermission(annoMain.annoPermission().enable());
        entity.setPermissionCode(annoMain.annoPermission().baseCode());
        entity.setPermissionCodeTranslate(annoMain.annoPermission().baseCodeTranslate());

        entity.setEnableLeftTree(annoMain.annoLeftTree().enable());
        entity.setLeftTreeCatKey(annoMain.annoLeftTree().catKey());
        entity.setLeftTreeClass(annoMain.annoLeftTree().treeClass());

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

        setAnFields(entity, clazz);

        List<AnButton> anButtons = getAnButton(clazz);
        entity.setButtons(anButtons);
        return entity;
    }

    /**
     * 设置字段信息和主键字段
     */
    private void setAnFields(AnEntity entity, Class<?> clazz) {
        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        List<AnField> anFields = new ArrayList<>();
        for (Field field : fields) {
            AnnoField anno = AnnotationUtil.getAnnotation(field, AnnoField.class);
            AnField anField = new AnField();
            anField.setFieldName(field.getName());
            anField.setTitle(anno.title());
            // 列名没有设置时，默认使用下划线
            if (StrUtil.isBlank(anno.tableFieldName())) {
                anField.setTableFieldName(StrUtil.toUnderlineCase(field.getName()));
            } else {
                anField.setTableFieldName(anno.tableFieldName());
            }
            anField.setFieldType(field.getType());
            anField.setFieldSize(anno.fieldSize());
            anField.setDefaultValue(anno.defaultValue());
            anField.setShow(anno.show());

            anField.setSearchEnable(anno.search().enable());
            anField.setSearchNotNull(anno.search().notNull());
            anField.setSearchPlaceHolder(anno.search().placeHolder());

            anField.setAddEnable(anno.edit().addEnable());
            anField.setEditEnable(anno.edit().editEnable());
            anField.setEditNotNull(anno.edit().notNull());
            anField.setEditPlaceHolder(anno.edit().placeHolder());

            anField.setDataType(anno.dataType());

            anField.setOptionTypeSql(anno.optionType().sql());
            AnnoOptionType.OptionData[] optionData = anno.optionType().value();
            if (ArrayUtil.isNotEmpty(optionData)) {
                List<AnField.OptionData> optionDataList = Arrays.stream(optionData)
                    .map(e -> new AnField.OptionData(e.label(), e.value()))
                    .collect(Collectors.toList());
                anField.setOptionDatas(optionDataList);
            }

            // 图像
            anField.setImageThumbRatio(anno.imageType().thumbRatio());
            anField.setImageThumbMode(anno.imageType().thumbMode());
            anField.setImageEnlargeAble(anno.imageType().enlargeAble());
            anField.setImageWidth(anno.imageType().width());
            anField.setImageHeight(anno.imageType().height());

            // 选择类型-树
            anField.setTreeTypeSql(anno.treeType().sql());
            AnnoTreeType.TreeData[] treeData = anno.treeType().value();
            if (ArrayUtil.isNotEmpty(treeData)) {
                List<AnField.TreeData> treeDataList = Arrays.stream(treeData)
                    .map(e -> new AnField.TreeData(e.id(), e.label(), e.value(), e.pid()))
                    .collect(Collectors.toList());
                anField.setTreeDatas(treeDataList);
            }

            // pk
            PrimaryKey primaryKey = AnnotationUtil.getAnnotation(field, PrimaryKey.class);
            if (primaryKey != null) {
                anField.setPrimaryKey(true);
                entity.setPkField(anField);
            }

            anFields.add(anField);
        }
        entity.setFields(anFields);
    }

    private List<AnButton> getAnButton(Class<?> clazz) {
        ArrayList<AnButton> anButtons = new ArrayList<>();
        List<Field> annoButtonFields = AnnoUtil.getAnnoButtonFields(clazz);
        for (Field buttonField : annoButtonFields) {
            AnnoButton anno = AnnotationUtil.getAnnotation(buttonField, AnnoButton.class);
            AnButton anButton = new AnButton();
            anButton.setName(anno.name());
            anButton.setPermissionCode(anno.permissionCode());
            anButton.setSize(anno.size());
            anButton.setJsCmd(anno.jsCmd());
            anButton.setJumpUrl(anno.jumpUrl());

            // 一对多
            anButton.setO2mEnable(anno.o2mJoinButton().enable());
            anButton.setO2mJoinMainClazz(anno.o2mJoinButton().joinAnnoMainClazz());
            anButton.setO2mJoinThisField(anno.o2mJoinButton().joinThisClazzField());
            anButton.setO2mJoinOtherField(anno.o2mJoinButton().joinOtherClazzField());

            // 多对多
            anButton.setM2mEnable(anno.m2mJoinButton().enable());
            anButton.setM2mJoinAnnoMainClazz(anno.m2mJoinButton().joinAnnoMainClazz());
            anButton.setM2mJoinSql(anno.m2mJoinButton().joinSql());
            anButton.setM2mJoinThisClazzField(anno.m2mJoinButton().joinThisClazzField());
            anButton.setM2mMediumTableClass(anno.m2mJoinButton().mediumTableClass());
            anButton.setM2mMediumOtherField(anno.m2mJoinButton().mediumOtherField());
            anButton.setM2mMediumThisField(anno.m2mJoinButton().mediumThisField());

            // java cmd
            anButton.setJavaCmdEnable(anno.javaCmd().enable());
            anButton.setJavaCmdBeanClass(anno.javaCmd().beanClass());
            anButton.setJavaCmdMethodName(anno.javaCmd().methodName());
            anButtons.add(anButton);
        }
        return anButtons;
    }
}
