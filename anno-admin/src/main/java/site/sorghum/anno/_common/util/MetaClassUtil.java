package site.sorghum.anno._common.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.annotation.Table;
import site.sorghum.anno._common.entity.ClassDef;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.AnMeta;
import site.sorghum.anno._metadata.AnnoJavaCmd;
import site.sorghum.anno._metadata.AnnoMtm;
import site.sorghum.anno.anno.annotation.clazz.*;
import site.sorghum.anno.anno.annotation.common.AnnoTplImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoChartFieldImpl;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeTypeImpl;
import site.sorghum.anno.anno.entity.common.FieldAnnoField;
import site.sorghum.anno.anno.tpl.BaseTplRender;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.anno.util.QuerySqlCache;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 元数据类工具类，用于处理类与元数据之间的转换
 */
@Slf4j
public class MetaClassUtil {
    // 需要忽略的方法名集合
    private static final Set<String> IGNORE_METHOD_NAME = new HashSet<>();

    // 当前类键名
    private static final String THIS_CLASS_KEY = "thisClass";

    // 动态类型缓存
    private static final Map<String, Class<?>> DY_TYPE_MAP = new HashMap<>();

    // 字段缓存
    private static final Map<String, Field> FIELD_MAP = new HashMap<>();

    static {
        // 初始化忽略的方法名
        IGNORE_METHOD_NAME.add("getClass");
        IGNORE_METHOD_NAME.add("toString");
        IGNORE_METHOD_NAME.add("hashCode");
        IGNORE_METHOD_NAME.add("annotationType");
        IGNORE_METHOD_NAME.add("pkField");
    }

    /**
     * 将Dict转换为AnMeta对象
     *
     * @param classed2Dict 包含类信息的字典
     * @return 转换后的AnMeta对象
     */
    @SneakyThrows
    public static AnMeta dict2AnMeta(Dict classed2Dict) {
        // 获取并移除原始类名
        String _thisClass = MapUtil.getStr(classed2Dict, THIS_CLASS_KEY);
        classed2Dict.remove(THIS_CLASS_KEY);

        // 转换为AnMeta对象
        AnMeta anMeta = JSONUtil.toBean(classed2Dict, AnMeta.class);

        // 加载实际的类对象
        Class<?> thisClass = loadActClass(_thisClass, anMeta);
        anMeta.setThisClass(thisClass);

        // 处理字段信息
        processFields(anMeta, thisClass);

        // 处理图表字段ID
        processChartFields(anMeta);

        // 处理按钮信息
        processButtons(anMeta);

        return anMeta;
    }

    /**
     * 将类对象转换为AnMeta对象
     *
     * @param clazz 要转换的类
     * @return 转换后的AnMeta对象
     */
    @SneakyThrows
    public static AnMeta class2AnMeta(Class<?> clazz) {
        Dict classed2Dict = class2Dict(clazz, true);
        return dict2AnMeta(classed2Dict);
    }

    /**
     * 将类对象转换为Dict
     *
     * @param clazz      要转换的类
     * @param deepSuper  是否处理父类
     * @return 转换后的Dict对象
     */
    @SneakyThrows
    public static Dict class2Dict(Class<?> clazz, boolean deepSuper) {
        // 获取主注解信息
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        if (annoMain == null) {
            AnnoForm annoForm = AnnoUtil.getAnnoForm(clazz);
            annoMain = AnnoMainImpl.builder().name(annoForm.name()).virtualTable(true).build();
        }

        StringBuilder yml = new StringBuilder();
        // 添加基础信息
        yml.append("thisClass=").append(clazz.getName()).append("\n");
        yml.append("entityName=").append(clazz.getSimpleName()).append("\n");
        yml.append("extend=").append(clazz.getSuperclass().getName()).append("\n");

        // 处理主注解
        printAnnotation(null, annoMain, yml);
        putDyTypeMap(clazz);

        // 处理表名
        processTableName(clazz, yml);

        // 处理字段注解
        processFieldAnnotations(clazz, deepSuper, yml);

        // 处理按钮字段
        processButtonFields(clazz, yml);

        // 处理移除注解
        AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
        printAnnotation("annoRemove", annoRemove, yml);

        // 转换为Properties再转为Dict
        Properties properties = new Properties();
        properties.load(new StringReader(yml.toString()));
        return JSONUtil.toBean(properties, Dict.class);
    }

    /**
     * 处理字段信息
     */
    private static void processFields(AnMeta anMeta, Class<?> thisClass) {
        List<AnField> columns = anMeta.getColumns();
        if (CollUtil.isEmpty(columns)) {
            return;
        }

        for (AnField column : columns) {
            // 设置字段对象
            Field field = getField(thisClass, column.getJavaName());
            column.setJavaField(field);

            // 设置主键字段
            if (column.pkField()) {
                anMeta.setPkColumn(column);
            }

            // 设置表字段名
            if (StrUtil.isBlank(column.getTableFieldName())) {
                column.setTableFieldName(StrUtil.toUnderlineCase(column.getJavaName()));
            }

            // 处理选项类型的SQL
            processOptionTypeSql(anMeta, column);

            // 处理树类型的SQL
            processTreeTypeSql(anMeta, column);

            // 处理枚举选项
            processEnumOptions(column);
        }
    }

    /**
     * 处理图表字段ID
     */
    private static void processChartFields(AnMeta anMeta) {
        AnnoChartFieldImpl[] annoChartFields = anMeta.getAnnoChart().getChartFields();
        for (AnnoChartFieldImpl annoChartField : annoChartFields) {
            annoChartField.setId(anMeta.getEntityName() + ":" +
                MD5Util.digestHex(annoChartField.getName() + annoChartField.getRunSupplier().getName()));
        }
    }

    /**
     * 处理按钮信息
     */
    private static void processButtons(AnMeta anMeta) {
        // 处理列按钮
        processColumnButtons(anMeta);

        // 处理表按钮
        processTableButtons(anMeta);
    }

    /**
     * 处理表名
     */
    private static void processTableName(Class<?> clazz, StringBuilder yml) {
        if (yml.toString().contains("tableName=\n")) {
            Table table = AnnotationUtil.getAnnotation(clazz, Table.class);
            if (Objects.nonNull(table)) {
                yml.append("tableName=").append(table.value()).append("\n");
            } else {
                yml.append("tableName=").append(StrUtil.toUnderlineCase(clazz.getSimpleName())).append("\n");
            }
        }
    }

    /**
     * 处理字段注解
     */
    private static void processFieldAnnotations(Class<?> clazz, boolean deepSuper, StringBuilder yml) {
        List<FieldAnnoField> annoFields = AnnoUtil.getAnnoFields(clazz, deepSuper);
        for (int i = 0; i < annoFields.size(); i++) {
            FieldAnnoField annoField = annoFields.get(i);
            yml.append("columns[%d].javaName=%s\n".formatted(i, annoField.getField().getName()));
            yml.append("columns[%d].jsonPath=%s\n".formatted(i, annoField.getField().getName()));
            yml.append("columns[%d].javaType=%s\n".formatted(i, annoField.getField().getType().getName()));

            if (annoField.getPrimaryKey() != null || annoField.getAnnoField().pkField()) {
                yml.append("columns[%d].pkField=%s\n".formatted(i, true));
            }

            printAnnotation("columns[%d]".formatted(i), annoField.getAnnoField(), yml);
            putFieldMap(clazz, annoField.getField());
        }
    }

    /**
     * 处理按钮字段
     */
    private static void processButtonFields(Class<?> clazz, StringBuilder yml) {
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz, false);
        for (int i = 0; i < buttonFields.size(); i++) {
            Field buttonField = buttonFields.get(i);
            yml.append("columnButtons[%d].javaName=%s\n".formatted(i, buttonField.getName()));
            yml.append("columnButtons[%d].javaType=%s\n".formatted(i, buttonField.getType().getName()));
            printAnnotation("columnButtons[%d]".formatted(i),
                AnnotationUtil.getAnnotation(buttonField, AnnoButton.class), yml);
        }
    }

    /**
     * 处理列按钮
     */
    private static void processColumnButtons(AnMeta anMeta) {
        List<AnnoButtonImpl> columnButtons = anMeta.getColumnButtons();
        if (CollUtil.isEmpty(columnButtons)) {
            return;
        }

        for (AnnoButtonImpl columnBtn : columnButtons) {
            // 处理多对多关联按钮
            processM2MButton(anMeta, columnBtn);

            // 处理Java命令按钮
            processJavaCmdButton(anMeta, columnBtn);

            // 处理模板按钮
            processTemplateButton(columnBtn);
        }
    }

    /**
     * 处理表按钮
     */
    private static void processTableButtons(AnMeta anMeta) {
        AnnoTableButtonImpl[] annoTableButtons = anMeta.getAnnoTableButton();
        if (annoTableButtons == null) {
            return;
        }

        List<AnnoTableButtonImpl> tableButtons = anMeta.getTableButtons();
        for (AnnoTableButtonImpl annoTableButton : annoTableButtons) {
            AnnoButtonImpl.JavaCmdImpl javaCmd = annoTableButton.getJavaCmd();
            if (javaCmd != null && javaCmd.isEnable()) {
                String id = anMeta.getEntityName() + ":" +
                    MD5Util.digestHex(annoTableButton.getName() + annoTableButton.getJavaCmd().runSupplier().getName());
                javaCmd.setId(id);
                AnnoJavaCmd.annoJavCmdMap.put(id, javaCmd);
                AnnoJavaCmd.annoJavaCmd2TableButtonMap.put(id, annoTableButton);
            }
            tableButtons.add(annoTableButton);
        }
    }

    /**
     * 处理选项类型的SQL
     */
    private static void processOptionTypeSql(AnMeta anMeta, AnField column) {
        AnnoOptionTypeImpl optionType = column.getOptionType();
        if (Objects.nonNull(optionType) && StrUtil.isNotBlank(optionType.anSql().sql())) {
            String sqlKey = getSqlKey(anMeta, column, optionType);
            optionType.setSqlKey(sqlKey);
            QuerySqlCache.put(sqlKey, optionType.anSql().sql());
        }
    }


    /**
     * 处理树类型的SQL
     */
    private static void processTreeTypeSql(AnMeta anMeta, AnField column) {
        AnnoTreeTypeImpl treeType = column.getTreeType();
        if (Objects.nonNull(treeType) && StrUtil.isNotBlank(treeType.anSql().sql())) {
            String sqlKey = QuerySqlCache.generateKey(treeType.anSql().dbName(),
                anMeta.getEntityName(), column.getJavaName(), treeType.anSql().sql());
            treeType.setSqlKey(sqlKey);
            QuerySqlCache.put(sqlKey, treeType.anSql().sql());
        }
    }

    /**
     * 获取SQL的Key值
     */
    private static String getSqlKey(AnMeta anMeta, AnField column, AnnoOptionTypeImpl optionType) {
        return QuerySqlCache.generateKey(optionType.anSql().dbName(),
            anMeta.getEntityName(), column.getJavaName(), optionType.anSql().sql());
    }

    /**
     * 处理枚举选项
     */
    private static void processEnumOptions(AnField column) {
        Class<? extends Enum> optionEnum = column.getOptionType().optionEnum();
        if (optionEnum != null && optionEnum != Enum.class) {
            List<AnnoOptionTypeImpl.OptionDataImpl> optionData = AnnoUtil.enum2OptionData(optionEnum);
            column.getOptionType().setValue(optionData.toArray(new AnnoOptionTypeImpl.OptionDataImpl[0]));
        }
    }

    /**
     * 处理多对多关联按钮
     */
    private static void processM2MButton(AnMeta anMeta, AnnoButtonImpl columnBtn) {
        AnnoButtonImpl.M2MJoinButtonImpl m2mJoinButton = columnBtn.getM2mJoinButton();
        if (m2mJoinButton != null && m2mJoinButton.isEnable()) {
            String id = anMeta.getEntityName() + ":" +
                MD5Util.digestHex(columnBtn.getName() + columnBtn.m2mJoinButton().mediumTableClazz());
            m2mJoinButton.setId(id);
            AnnoMtm.annoMtmMap.put(id, m2mJoinButton);
        }
    }

    /**
     * 处理Java命令按钮
     */
    private static void processJavaCmdButton(AnMeta anMeta, AnnoButtonImpl columnBtn) {
        AnnoButtonImpl.JavaCmdImpl javaCmd = columnBtn.getJavaCmd();
        if (javaCmd != null && javaCmd.isEnable()) {
            String id = anMeta.getEntityName() + ":" +
                MD5Util.digestHex(columnBtn.getName() + columnBtn.getJavaCmd().runSupplier().getName());
            javaCmd.setId(id);
            AnnoJavaCmd.annoJavCmdMap.put(id, javaCmd);
            AnnoJavaCmd.annoJavaCmd2ButtonMap.put(id, columnBtn);
        }
    }

    /**
     * 处理模板按钮
     */
    private static void processTemplateButton(AnnoButtonImpl columnBtn) {
        AnnoTplImpl annoTpl = columnBtn.getAnnoTpl();
        if (annoTpl != null && annoTpl.isEnable()) {
            String id = BaseTplRender.toId(annoTpl.getTplClazz());
            annoTpl.setId(id);
        }
    }

    /**
     * 打印注解信息到StringBuilder
     *
     * @param superName   父级名称
     * @param annotation 注解对象
     * @param yml        输出的StringBuilder
     */
    @SneakyThrows
    private static void printAnnotation(String superName, Annotation annotation, StringBuilder yml) {
        if (annotation == null) {
            return;
        }

        for (Method publicMethod : ReflectUtil.getPublicMethods(annotation.getClass())) {
            String name = StrUtil.isNotBlank(superName) ?
                superName + "." + publicMethod.getName() : publicMethod.getName();

            if (IGNORE_METHOD_NAME.contains(publicMethod.getName()) ||
                publicMethod.getParameterCount() > 0) {
                continue;
            }

            Object invoke;
            try {
                invoke = publicMethod.invoke(annotation);
            } catch (Exception e) {
                continue;
            }

            processOne(invoke, name, yml);
        }
    }

    /**
     * 处理单个注解值
     */
    private static void processOne(Object invoke, String name, StringBuilder yml) {
        if (invoke == null) {
            return;
        }

        if (invoke instanceof Annotation _annotation) {
            printAnnotation(name, _annotation, yml);
        } else if (invoke instanceof Object[] objectArray) {
            for (int i = 0; i < objectArray.length; i++) {
                processOne(objectArray[i], name + "[%d]".formatted(i), yml);
            }
        } else if (invoke.getClass().isArray()) {
            for (int i = 0; ; i++) {
                try {
                    Object item = Array.get(invoke, i);
                    processOne(item, name + "[%d]".formatted(i), yml);
                } catch (Exception e) {
                    break;
                }
            }
        } else if (invoke instanceof Class<?> _clazz) {
            yml.append(name).append("=").append(_clazz.getName()).append("\n");
        } else {
            yml.append(name).append("=").append(invoke).append("\n");
        }
    }

    /**
     * 添加动态类型到缓存
     */
    public static void putDyTypeMap(Class<?> clazz) {
        DY_TYPE_MAP.put(clazz.getName(), clazz);
    }

    /**
     * 添加字段到缓存
     */
    public static void putFieldMap(Class<?> clazz, Field filed) {
        FIELD_MAP.put(clazz.getName() + filed.getName(), filed);
    }

    /**
     * 获取字段对象
     */
    public static Field getField(Class<?> clazz, String name) {
        Field field = FIELD_MAP.get(clazz.getName() + name);
        if (field == null) {
            return ReflectUtil.getField(clazz, name);
        }
        return field;
    }

    /**
     * 加载实际的类对象
     */
    public static Class<?> loadActClass(String _thisClass, AnMeta anMeta) {
        Class<?> thisClass = DY_TYPE_MAP.get(_thisClass);
        if (thisClass != null) {
            return thisClass;
        }

        try {
            return Class.forName(_thisClass);
        } catch (ClassNotFoundException ignore) {
            if (anMeta == null) {
                throw new BizException("找不到类: " + _thisClass);
            }
            return DynamicClassGenerator.addClass(_thisClass,
                ClassDef.anMeta2Class(_thisClass, anMeta));
        }
    }
}