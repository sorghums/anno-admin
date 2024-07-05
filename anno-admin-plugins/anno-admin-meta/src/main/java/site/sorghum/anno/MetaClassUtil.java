package site.sorghum.anno;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.method.resource.ResourceFinder;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class MetaClassUtil {

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

    public static void main(String[] args) {
        Map<TypeDescription, Class<?>> typeDescriptionClassMap = MetaClassUtil.loadSystemClass();
        System.out.println(typeDescriptionClassMap);
    }
}
