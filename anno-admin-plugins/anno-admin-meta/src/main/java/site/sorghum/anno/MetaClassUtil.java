package site.sorghum.anno;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.alibaba.fastjson2.util.PropertiesUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnChart;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.entity.common.FieldAnnoField;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.method.resource.ResourceFinder;
import site.sorghum.anno.plugin.ao.AnLoginChart;
import site.sorghum.anno.plugin.ao.AnOnlineUser;
import site.sorghum.anno.plugin.ao.AnUser;

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

@Slf4j
public class MetaClassUtil {
    private static Set<String> IGNORE_METHOD_NAME = new HashSet<>();
    static {
        IGNORE_METHOD_NAME.add("getClass");
        IGNORE_METHOD_NAME.add("toString");
        IGNORE_METHOD_NAME.add("hashCode");
        IGNORE_METHOD_NAME.add("annotationType");
    }

    @SneakyThrows
    public static Map<TypeDescription, Class<?>> loadSystemClass() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        // 遍历根目录下所有资源，并过滤保留符合条件的资源
        MultiResource multiResource = ResourceFinder.of().find("dynamic/*.yml");
        final DynamicType.Unloaded<?>[] dynamicType = {null};
        multiResource.iterator().forEachRemaining(resource -> {
            try {
                log.info("加载：{}", resource);
                if (resource.getName().endsWith(".yml")) {
                    Dict dict = YamlUtil.load(resource.getReader(Charset.defaultCharset()));
                    AnMeta anMeta = JSONUtil.toBean(dict, AnMeta.class);
                    DynamicType.Builder<?> builder;
                    ByteBuddy byteBuddy = new ByteBuddy();
                    if (StrUtil.isNotBlank(anMeta.getExtend())) {
                        classLoader.loadClass(anMeta.getExtend());
                        builder = byteBuddy.subclass(Class.forName(anMeta.getExtend()));
                    } else {
                        builder = byteBuddy.subclass(Object.class);
                    }
                    builder = builder.name("site.sorghum.anno.meta.proxy.%s".formatted(anMeta.getEntityName()));
                    // 定义annoMain
                    builder = builder.annotateType(anMeta);
                    // 定义annoField
                    List<AnMeta.Column> anMetaColumns = anMeta.getColumns();
                    for (AnMeta.Column column : anMetaColumns) {
                        builder = builder.defineField(
                            column.getJavaName(), column.getJavaType(), Modifier.PUBLIC
                        ).annotateField(column);
                    }
                    List<AnnoButtonImpl> columnButtons = anMeta.getColumnButtons();
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
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        dynamicType[0].toJar(
            new File(FileUtil.file("dynamic.jar").getAbsolutePath())
        );
        return dynamicType[0].load(classLoader)
            .getAllLoaded();
    }

    @SneakyThrows
    public static Map<TypeDescription, Class<?>> loadClass(String ymlContent) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        final DynamicType.Unloaded<?>[] dynamicType = {null};
        try {
            log.info("加载：{}", ymlContent);
            Dict dict = YamlUtil.load(new StringReader(ymlContent));
            AnMeta anMeta = JSONUtil.toBean(dict, AnMeta.class);
            DynamicType.Builder<?> builder;
            ByteBuddy byteBuddy = new ByteBuddy();
            if (StrUtil.isNotBlank(anMeta.getExtend())) {
                classLoader.loadClass(anMeta.getExtend());
                builder = byteBuddy.subclass(Class.forName(anMeta.getExtend()));
            } else {
                builder = byteBuddy.subclass(Object.class);
            }
            builder = builder.name("site.sorghum.anno.meta.proxy.%s".formatted(anMeta.getEntityName()));
            // 定义annoMain
            builder = builder.annotateType(anMeta);
            // 定义annoField
            List<AnMeta.Column> anMetaColumns = Optional.ofNullable(anMeta.getColumns()).orElse(Collections.emptyList());
            for (AnMeta.Column column : anMetaColumns) {
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
        dynamicType[0].
            toJar(
                new File(FileUtil.file("dynamic.jar").
                    getAbsolutePath())
            );
        return dynamicType[0].
            load(classLoader).
            getAllLoaded();
    }


    @SneakyThrows
    public static String class2yml(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        StringBuilder yml = new StringBuilder();
        yml.append("entityName=").append(clazz.getSimpleName()).append("\n");
        yml.append("extend=").append(clazz.getSuperclass().getName()).append("\n");
        printAnnotation(null, annoMain, yml);
        List<FieldAnnoField> annoFields = AnnoUtil.getAnnoFields(clazz,false);
        int i = 0;
        for (FieldAnnoField annoField : annoFields) {
            int nowValue = i++;
            yml.append("columns[%d].javaName=%s\n".formatted(nowValue,annoField.getField().getName()));
            yml.append("columns[%d].javaType=%s\n".formatted(nowValue,annoField.getField().getType().getName()));
            printAnnotation("columns[%d]".formatted(nowValue), annoField.getAnnoField(), yml);
        }
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz,false);
        i = 0;
        for (Field buttonField : buttonFields) {
            int nowValue = i++;
            yml.append("columnButtons[%d].javaName=%s\n".formatted(nowValue,buttonField.getName()));
            yml.append("columnButtons[%d].javaType=%s\n".formatted(nowValue,buttonField.getType().getName()));
            printAnnotation("columnButtons[%d]".formatted(nowValue), AnnotationUtil.getAnnotation(buttonField, AnnoButton.class), yml);
        }
        Properties properties = new Properties();
        properties.load(new StringReader(yml.toString()));
        Dict dict = JSONUtil.toBean(properties, Dict.class);
        StringWriter writer = new StringWriter();
        YamlUtil.dump(dict,writer);
        return writer.toString();
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

    private static void processOne(Object invoke, String name, StringBuilder yml){
        if (invoke instanceof Annotation _annotation) {
            printAnnotation(name, _annotation, yml);
        } else if (invoke instanceof Object[] objectArray) {
            int i = 0;
            for (Object item : objectArray) {
                processOne(item, name+"[%d]".formatted(i++), yml);
            }
        } else if (invoke.getClass().isArray()) {
            int i = 0;
            while (true){
                try {
                    Object item = Array.get(invoke, i);
                    processOne(item, name+"[%d]".formatted(i++), yml);
                }catch (Exception e){
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
        String string = class2yml(AnOnlineUser.class);
        System.out.println(string);
    }
}
