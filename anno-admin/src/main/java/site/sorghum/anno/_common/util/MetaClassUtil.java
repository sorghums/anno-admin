package site.sorghum.anno._common.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.noear.wood.annotation.Table;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.AnMeta;
import site.sorghum.anno._metadata.AnnoJavaCmd;
import site.sorghum.anno._metadata.AnnoMtm;
import site.sorghum.anno.anno.annotation.clazz.AnnoForm;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoMainImpl;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.common.AnnoTplImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoChartFieldImpl;
import site.sorghum.anno.anno.entity.common.FieldAnnoField;
import site.sorghum.anno.anno.tpl.BaseTplRender;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.method.resource.ResourceFinder;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MetaClassUtil {
    private static final Set<String> IGNORE_METHOD_NAME = new HashSet<>();

    static {
        IGNORE_METHOD_NAME.add("getClass");
        IGNORE_METHOD_NAME.add("toString");
        IGNORE_METHOD_NAME.add("hashCode");
        IGNORE_METHOD_NAME.add("annotationType");
        IGNORE_METHOD_NAME.add("pkField");
    }

    @SneakyThrows
    public static Map<TypeDescription, Class<?>> loadSystemClass() {
        // 遍历根目录下所有资源，并过滤保留符合条件的资源
        MultiResource multiResource = ResourceFinder.of().find("dynamic/*.yml");
        Map<TypeDescription, Class<?>> map = new HashMap<>();
        final DynamicType.Unloaded<?>[] dynamicType = {null};
        multiResource.iterator().forEachRemaining(resource -> {
            try {
                log.info("加载：{}", resource);
                if (resource.getName().endsWith(".yml")) {
                    String content = resource.getReader(Charset.defaultCharset()).lines().collect(Collectors.joining());
                    map.putAll(loadClass(content, false));
                    ;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        return map;
    }

    @SneakyThrows
    public static Map<TypeDescription, Class<?>> loadClass(String ymlContent) {
        return loadClass(ymlContent, false);
    }

    @SneakyThrows
    public static Map<TypeDescription, Class<?>> loadClass(String ymlContent, boolean toJar) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        final DynamicType.Unloaded<?>[] dynamicType = {null};
        try {
            Dict dict = YamlUtil.load(new StringReader(ymlContent));
            AnMeta anMeta = JSONUtil.toBean(dict, AnMeta.class);
            DynamicType.Builder<?> builder;
            ByteBuddy byteBuddy = new ByteBuddy();
            if (Objects.nonNull(anMeta.getExtend())) {
                classLoader.loadClass(anMeta.getExtend().getName());
                builder = byteBuddy.subclass(anMeta.getExtend());
            } else {
                builder = byteBuddy.subclass(Object.class);
            }
            builder = builder.name("site.sorghum.anno.meta.proxy.%s".formatted(anMeta.getEntityName()));
            // 定义annoMain
            builder = builder.annotateType(anMeta);
            // 定义annoField
            List<AnField> anMetaColumns = Optional.ofNullable(anMeta.getColumns()).orElse(Collections.emptyList());
            for (AnField column : anMetaColumns) {
                builder = builder.defineField(
                    column.getJavaName(), column.getJavaType(), Modifier.PUBLIC
                ).annotateField(column);
            }
            List<AnnoButtonImpl> columnButtons = Optional.ofNullable(anMeta.getColumnButtons()).orElse(Collections.emptyList());
            for (AnnoButtonImpl columnBtn : columnButtons) {
                builder = builder.defineField(
                    columnBtn.getName(), Object.class, Modifier.PUBLIC
                ).annotateField(columnBtn);
            }
            if (Objects.isNull(dynamicType[0])) {
                dynamicType[0] = builder.make();
            } else {
                dynamicType[0].include(builder.make());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (toJar) {
            dynamicType[0].
                toJar(
                    new File(FileUtil.file("dynamic.jar").
                        getAbsolutePath())
                );
        }
        return dynamicType[0].
            load(classLoader).
            getAllLoaded();
    }

    @SneakyThrows
    public static String class2Yml(Class<?> clazz) {
        Dict dict = class2Dict(clazz, false);
        StringWriter writer = new StringWriter();
        YamlUtil.dump(dict, writer);
        return writer.toString();
    }

    @SneakyThrows
    public static String class2Json(Class<?> clazz) {
        return JSONUtil.toJsonString(class2Dict(clazz, false));
    }

    public static AnMeta class2AnMeta(Class<?> clazz) {
        AnMeta anMeta = JSONUtil.toBean(class2Dict(clazz, true), AnMeta.class);
        // 重新设置 tableName
        if (StrUtil.isBlank(anMeta.getTableName())) {
            // @Table 获取
            Table table = AnnotationUtil.getAnnotation(anMeta.getThisClass(), Table.class);
            if (Objects.nonNull(table)) {
                anMeta.setTableName(table.value());
            }else {
                anMeta.setTableName(StrUtil.toUnderlineCase(anMeta.getEntityName()));;
            }
        }
        // 重新设置 javaField
        List<AnField> columns = anMeta.getColumns();
        if (CollUtil.isNotEmpty(columns)) {
            for (AnField column : columns) {
                Field field = ReflectUtil.getField(clazz, column.getJavaName());
                column.setJavaField(field);
                if (column.pkField()) {
                    anMeta.setPkColumn(column);
                }
                // 重新设置tableFieldName
                if (StrUtil.isBlank(column.getTableFieldName())) {
                    column.setTableFieldName(StrUtil.toUnderlineCase(column.getJavaName()));;
                }
            }
        }
        // 重新设置 chart的ID
        AnnoChartFieldImpl[] annoChartFields = anMeta.getAnnoChart().getChartFields();
        for (AnnoChartFieldImpl annoChartField : annoChartFields) {
            annoChartField.setId(anMeta.getEntityName() + ":" + MD5Util.digestHex(annoChartField.getName() + annoChartField.getRunSupplier().getName()));
        }

        // 重新设置m2m的ID
        List<AnnoButtonImpl> columnButtons = anMeta.getColumnButtons();
        if (CollUtil.isNotEmpty(columnButtons)) {
            for (AnnoButtonImpl columnBtn : columnButtons) {
                AnnoButtonImpl.M2MJoinButtonImpl m2mJoinButton = columnBtn.getM2mJoinButton();
                if (m2mJoinButton != null && m2mJoinButton.isEnable()) {
                    String id = anMeta.getEntityName() + ":" + MD5Util.digestHex(columnBtn.getName() + columnBtn.hashCode());
                    m2mJoinButton.setId(id);
                    AnnoMtm.annoMtmMap.put(id, m2mJoinButton);
                }

                AnnoButtonImpl.JavaCmdImpl javaCmd = columnBtn.getJavaCmd();
                if (javaCmd != null && javaCmd.isEnable()) {
                    String id = anMeta.getEntityName() + ":" + MD5Util.digestHex(columnBtn.getName() + columnBtn.hashCode());
                    javaCmd.setId(id);
                    AnnoJavaCmd.annoJavCmdMap.put(id, javaCmd);
                    AnnoJavaCmd.annoJavaCmd2ButtonMap.put(id, columnBtn);
                }

                AnnoTplImpl annoTpl = columnBtn.getAnnoTpl();
                if (annoTpl != null && annoTpl.isEnable()){
                    BaseTplRender tplRender = AnnoBeanUtils.getBean(annoTpl.getTplClazz());
                    annoTpl.setId(tplRender.getId());
                }
            }
        }
        return anMeta;
    }


    @SneakyThrows
    public static Dict class2Dict(Class<?> clazz, boolean deepSuper) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        if (annoMain == null) {
            AnnoForm annoForm = AnnoUtil.getAnnoForm(clazz);
            annoMain = AnnoMainImpl.builder().name(annoForm.name()).virtualTable(true).build();
        }
        StringBuilder yml = new StringBuilder();
        yml.append("thisClass=").append(clazz.getName()).append("\n");
        yml.append("entityName=").append(clazz.getSimpleName()).append("\n");
        yml.append("extend=").append(clazz.getSuperclass().getName()).append("\n");
        printAnnotation(null, annoMain, yml);
        List<FieldAnnoField> annoFields = AnnoUtil.getAnnoFields(clazz, deepSuper);
        int i = 0;
        for (FieldAnnoField annoField : annoFields) {
            int nowValue = i++;
            yml.append("columns[%d].javaName=%s\n".formatted(nowValue, annoField.getField().getName()));
            yml.append("columns[%d].javaType=%s\n".formatted(nowValue, annoField.getField().getType().getName()));
            // 手动设置主键
            if (annoField.getPrimaryKey() != null) {
                yml.append("columns[%d].pkField=%s\n".formatted(nowValue, true));
            }
            printAnnotation("columns[%d]".formatted(nowValue), annoField.getAnnoField(), yml);
        }
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz, false);
        i = 0;
        for (Field buttonField : buttonFields) {
            int nowValue = i++;
            yml.append("columnButtons[%d].javaName=%s\n".formatted(nowValue, buttonField.getName()));
            yml.append("columnButtons[%d].javaType=%s\n".formatted(nowValue, buttonField.getType().getName()));
            printAnnotation("columnButtons[%d]".formatted(nowValue), AnnotationUtil.getAnnotation(buttonField, AnnoButton.class), yml);
        }
        // 设置annoRemove信息
        AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
        printAnnotation("annoRemove", annoRemove, yml);
        Properties properties = new Properties();
        properties.load(new StringReader(yml.toString()));
        return JSONUtil.toBean(properties, Dict.class);
    }

    @SneakyThrows
    private static void printAnnotation(String superName, Annotation annotation, StringBuilder yml) {
        for (Method publicMethod : ReflectUtil.getPublicMethods(annotation.getClass())) {
            String name = StrUtil.isNotBlank(superName) ? superName + "." + publicMethod.getName() : publicMethod.getName();
            if (IGNORE_METHOD_NAME.contains(publicMethod.getName()) || publicMethod.getParameterCount() > 0) {
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

    private static void processOne(Object invoke, String name, StringBuilder yml) {
        if (invoke == null) {
            return;
        }
        if (invoke instanceof Annotation _annotation) {
            printAnnotation(name, _annotation, yml);
        } else if (invoke instanceof Object[] objectArray) {
            int i = 0;
            for (Object item : objectArray) {
                processOne(item, name + "[%d]".formatted(i++), yml);
            }
        } else if (invoke.getClass().isArray()) {
            int i = 0;
            while (true) {
                try {
                    Object item = Array.get(invoke, i);
                    processOne(item, name + "[%d]".formatted(i++), yml);
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


    public static void main(String[] args) {

    }
}
