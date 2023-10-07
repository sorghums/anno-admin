package site.sorghum.anno.anno.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoMany2ManyField;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.entity.common.FieldAnnoField;
import site.sorghum.anno.db.param.DbCondition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Anno工具
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class AnnoUtil {

    /**
     * 得到Anno注解
     *
     * @param clazz clazz
     * @return {@link AnnoMain}
     */
    public static AnnoMain getAnnoMain(Class<?> clazz) {
        return AnnotationUtil.getSynthesizedAnnotation(clazz, AnnoMain.class);
    }

    public static AnnoRemove getAnnoRemove(Class<?> clazz) {
        List<Class<?>> allClass = findAllClass(clazz);
        for (Class<?> aClass : allClass) {
            AnnoRemove annoRemove = AnnotationUtil.getAnnotation(aClass, AnnoRemove.class);
            if (annoRemove != null) {
                return annoRemove;
            }
        }
        return new AnnoRemove() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnoRemove.class;
            }

            @Override
            public int removeType() {
                return 0;
            }

            @Override
            public String removeValue() {
                return null;
            }

            @Override
            public String notRemoveValue() {
                return null;
            }

            @Override
            public String removeField() {
                return null;
            }
        };
    }

    /**
     * 得到表名
     *
     * @param clazz clazz
     * @return {@link String}
     */
    public static String getTableName(Class<?> clazz) {
        Table table = AnnotationUtil.getAnnotation(clazz, Table.class);
        if (table == null) {
            throw new BizException("请在类上添加@Table注解");
        }
        return table.value();
    }

    /**
     * 得到表名
     *
     * @param field clazz
     * @return {@link String}
     */
    public static String getColumnName(Field field) {
        AnnoField annoField = AnnotationUtil.getAnnotation(field, AnnoField.class);
        if (annoField == null) {
            throw new BizException("请在类上添加@AnnoField注解");
        }
        return annoField.tableFieldName();
    }

    /**
     * 得到表名
     *
     * @param field clazz
     * @return {@link String}
     */
    public static String getColumnName(FieldAnnoField field) {
        return field.getAnnoField().tableFieldName();
    }

    /**
     * 获取表字段
     *
     * @param clazz 类
     * @return {@link List<String>}
     */
    public static List<FieldAnnoField> getAnnoFields(Class<?> clazz) {
        List<FieldAnnoField> annoFieldFields = CollUtil.newArrayList();
        List<Class<?>> allClass = AnnoUtil.findAllClass(clazz);
        for (Class<?> aClass : allClass) {
            Field[] declaredFields = ClassUtil.getDeclaredFields(aClass);
            for (Field declaredField : declaredFields) {
                AnnoField annotation = AnnotationUtil.getAnnotation(declaredField, AnnoField.class);
                PrimaryKey primaryKey = AnnotationUtil.getAnnotation(declaredField, PrimaryKey.class);
                if (annotation != null) {
                    annoFieldFields.add(new FieldAnnoField(declaredField,annotation,primaryKey));
                }
            }
            // 扫描方法
            Method[] declaredMethods = ClassUtil.getDeclaredMethods(aClass);
            for (Method declaredMethod : declaredMethods) {
                AnnoField annotation = AnnotationUtil.getAnnotation(declaredMethod, AnnoField.class);
                PrimaryKey primaryKey = AnnotationUtil.getAnnotation(declaredMethod, PrimaryKey.class);
                if (annotation != null) {
                    // 根据method 查找对应的field
                    String name = declaredMethod.getName();
                    if (name.startsWith("get")) {
                        name = StrUtil.removePrefix(name, "get");
                        // 首字符小写
                        name = StrUtil.lowerFirst(name);
                    }
                    if (name.startsWith("is")) {
                        name = StrUtil.removePrefix(name, "is");
                        // 首字符小写
                        name = StrUtil.lowerFirst(name);
                    }
                    Field field = ReflectUtil.getField(aClass, name);
                    if (field == null){
                        throw new BizException("%s,未找到对应的field".formatted(annotation.title()));
                    }
                    annoFieldFields.add(new FieldAnnoField(field,annotation,primaryKey));
                }
            }
        }
        return annoFieldFields;
    }

    /**
     * 获取表字段
     *
     * @param clazz 类
     * @return {@link List<String>}
     */
    public static List<Field> getAnnoMany2ManyFields(Class<?> clazz) {
        List<Field> annoFieldFields = CollUtil.newArrayList();
        List<Class<?>> allClass = AnnoUtil.findAllClass(clazz);
        for (Class<?> aClass : allClass) {
            Field[] declaredFields = ClassUtil.getDeclaredFields(aClass);
            for (Field declaredField : declaredFields) {
                AnnoMany2ManyField annotation = AnnotationUtil.getAnnotation(declaredField, AnnoMany2ManyField.class);
                if (annotation != null) {
                    annoFieldFields.add(declaredField);
                }
            }
        }
        return annoFieldFields;
    }

    /**
     * 获取表字段
     *
     * @param clazz 类
     * @return {@link List<String>}
     */
    public static List<Field> getAnnoButtonFields(Class<?> clazz) {
        List<Field> annoFieldFields = CollUtil.newArrayList();
        List<Class<?>> allClass = AnnoUtil.findAllClass(clazz);
        for (Class<?> aClass : allClass) {
            Field[] declaredFields = ClassUtil.getDeclaredFields(aClass);
            for (Field declaredField : declaredFields) {
                AnnoButton annotation = AnnotationUtil.getAnnotation(declaredField, AnnoButton.class);
                if (annotation != null) {
                    annoFieldFields.add(declaredField);
                }
            }
        }
        return annoFieldFields;
    }


    /**
     * 得到主键字段
     *
     * @param clazz clazz
     * @return {@link String}
     */
    public static String getPkField(Class<?> clazz) {
        List<FieldAnnoField> declaredFields = AnnoUtil.getAnnoFields(clazz);
        Optional<FieldAnnoField> first = declaredFields.stream().filter(field -> field.getPrimaryKey() != null).findFirst();
        return first.map(fieldAnnoField -> fieldAnnoField.getField().getName()).orElseThrow(() -> new BizException("未找到主键"));
    }



    /**
     * 得到父主键字段
     *
     * @param clazz clazz
     * @return {@link String}
     */
    public static String getParentPk(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        return annoMain.annoTree().parentKey();
    }

    public static <T> List<AnnoTreeDTO<String>> buildAnnoTree(List<T> data,
                                                              String label,
                                                              String key,
                                                              String parentKey) {
        List<AnnoTreeDTO<String>> annoTreeDTOS = list2AnnoTreeNode(data, label, key, parentKey);
        return listToTree(annoTreeDTOS);
    }

    public static List<AnnoTreeDTO<String>> listToTree(List<AnnoTreeDTO<String>> list) {
        Map<String, AnnoTreeDTO<String>> map = new HashMap<>();
        List<AnnoTreeDTO<String>> roots = new ArrayList<>();
        for (AnnoTreeDTO<String> node : list) {
            map.put(node.getId(), node);
        }
        for (AnnoTreeDTO<String> node : list) {
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                AnnoTreeDTO<String> parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    private static <T> List<AnnoTreeDTO<String>> list2AnnoTreeNode(List<T> datas,
                                                                   String label,
                                                                   String key,
                                                                   String parentKey) {
        return datas.stream().map(
            d -> {
                AnnoTreeDTO<String> annoTreeDto = new AnnoTreeDTO<>();
                annoTreeDto.setId(simpleToString(reflectGetValue(d, key)));
                annoTreeDto.setLabel(simpleToString(reflectGetValue(d, label)));
                annoTreeDto.setValue(simpleToString(reflectGetValue(d, key)));
                annoTreeDto.setParentId(simpleToString(reflectGetValue(d, parentKey)));
                annoTreeDto.setChildren(new ArrayList<>());
                return annoTreeDto;
            }
        ).collect(Collectors.toList());
    }

    private static boolean isRootNode(Object value) {
        return ObjectUtil.isEmpty(value) || "0".equals(value.toString());
    }

    public static List<Class<?>> findAllClass(Class<?> clazz) {
        List<Class<?>> allSuperClass = CollUtil.newArrayList(clazz);
        while (true) {
            // 如果父类是Object，就跳出循环
            Class<?> pClazz;
            if ((pClazz = clazz.getSuperclass()) == Object.class) {
                break;
            }
            allSuperClass.add(pClazz);
            clazz = pClazz;
        }
        return allSuperClass;
    }

    public static String simpleToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getTreeClass(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        if (annoMain.annoTree().enable() && annoMain.annoTree().displayAsTree()) {
            return clazz.getSimpleName();
        }
        if (annoMain.annoLeftTree().enable()) {
            return annoMain.annoLeftTree().treeClass().getSimpleName();
        }
        return "";
    }

    private static Object reflectGetValue(Object o, String field) {
        if (o instanceof Map) {
            return ((Map<?, ?>) o).get(field);
        }
        return ReflectUtil.getFieldValue(o, field);
    }

    /**
     * 简单实体转条件
     *
     * @param entity 参数
     * @param clazz  类
     * @param <T>    泛型
     */
    @SneakyThrows
    public static <T> List<DbCondition> simpleEntity2conditions(Object entity, Class<T> clazz) {
        ArrayList<DbCondition> conditions = new ArrayList<>();
        if (entity instanceof Map map) {
            entity = JSONUtil.toBean(JSONUtil.toJsonString(map), clazz);
        }
        if (entity.getClass() != clazz) {
            throw new IllegalArgumentException("entity must be instance of " + clazz.getName());
        }
        MetadataManager metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<AnField> anFields;
        if (anEntity.isVirtualTable()){
            anFields = anEntity.getFields();
        }else {
            anFields = anEntity.getDbAnFields();
        }
        for (AnField anField : anFields) {
            String sqlColumn = anField.getTableFieldName();
            Object value = ReflectUtil.getFieldValue(entity, anField.getFieldName());
            if (sqlColumn != null && value != null) {
                conditions.add(DbCondition.builder().field(sqlColumn).value(value).build());
            }
        }
        return conditions;
    }
}
