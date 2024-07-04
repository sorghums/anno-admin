package site.sorghum.anno;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.annotation.AnnoIgnore;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

@Slf4j
public class DemoUtil {

    @SneakyThrows
    public static Map<TypeDescription, Class<?>> loadClass() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Dict dict = YamlUtil.loadByPath("D:\\Project\\rep\\opensource\\anno-admin\\anno-admin-plugins\\anno-admin-meta\\src\\main\\resources\\AnUserMeta.yml");
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
        DynamicType.Unloaded<?> dynamicType = builder
            .make();
        // 打印dynamicType
        dynamicType.toJar(new File("D:\\Project\\rep\\opensource\\anno-admin\\anno-admin-plugins\\anno-admin-meta\\src\\main\\resources\\_anno_proxy.jar"));
        return dynamicType.load(classLoader)
            .getAllLoaded();
    }

    private static synchronized AnnotationDescription.Builder entity2AnnotationDescriptionBuilder(AnnotationDescription.Builder builder, Object entity) {
        Field[] fields = ReflectUtil.getFields(entity.getClass(), f -> !AnnotationUtil.hasAnnotation(f, AnnoIgnore.class));
        log.info("准备注入：{}", ReflectUtil.getFieldValue(builder, "annotationType"));
        for (Field field : fields) {
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            if (fieldValue == null) {
                continue;
            }

            try {
                if (fieldValue instanceof Boolean fieldValueBoolean) {
                    builder = builder.define(field.getName(), fieldValueBoolean);
                    log.info("注入{}：{}", field.getName(), fieldValueBoolean);
                }

                if (fieldValue instanceof String fieldValueString) {
                    builder = builder.define(field.getName(), fieldValueString);
                    log.info("注入{}：{}", field.getName(), fieldValueString);
                }


                if (fieldValue instanceof Integer fieldValueInteger) {
                    builder = builder.define(field.getName(), fieldValueInteger);
                    log.info("注入{}：{}", field.getName(), fieldValueInteger);
                }

                if (fieldValue instanceof Long fieldValueLong) {
                    builder = builder.define(field.getName(), fieldValueLong);
                    log.info("注入{}：{}", field.getName(), fieldValueLong);
                }

                if (fieldValue instanceof Float fieldValueFloat) {
                    builder = builder.define(field.getName(), fieldValueFloat);
                    log.info("注入{}：{}", field.getName(), fieldValueFloat);
                }

                if (fieldValue instanceof Double fieldValueDouble) {
                    builder = builder.define(field.getName(), fieldValueDouble);
                    log.info("注入{}：{}", field.getName(), fieldValueDouble);
                }

                if (fieldValue instanceof Short fieldValueShort) {
                    builder = builder.define(field.getName(), fieldValueShort);
                    log.info("注入{}：{}", field.getName(), fieldValueShort);
                }

                if (fieldValue instanceof Byte fieldValueByte) {
                    builder = builder.define(field.getName(), fieldValueByte);
                    log.info("注入{}：{}", field.getName(), fieldValueByte);
                }

                if (fieldValue instanceof Annotation fieldValueAnnotation) {
                    builder = builder.define(field.getName(), fieldValueAnnotation);
                    log.info("注入{}：{}", field.getName(), fieldValueAnnotation);
                }

                if (fieldValue instanceof Enum<?> fieldValueEnum) {
                    builder = builder.define(field.getName(), fieldValueEnum);
                    log.info("注入{}：{}", field.getName(), fieldValueEnum);
                }

                if (fieldValue instanceof Class<?> fieldValueClass) {
                    builder = builder.define(field.getName(), fieldValueClass);
                    log.info("注入{}：{}", field.getName(), fieldValueClass);
                }
            } catch (IllegalArgumentException e) {
                log.warn(
                    "注解：{} 注入失败:{}", builder, field
                );
            }

        }
        return builder;
    }

    public static void main(String[] args) {
        Map<TypeDescription, Class<?>> typeDescriptionClassMap = DemoUtil.loadClass();
        System.out.println(typeDescriptionClassMap);
    }
}
