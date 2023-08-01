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
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPreProxy;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.anno.proxy.AnnoPreDefaultProxy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return AnnotationUtil.getAnnotation(clazz, AnnoMain.class);
    }

    /**
     * 获得代理实例
     *
     * @param clazz clazz
     * @return {@link AnnoBaseProxy}<{@link T}>
     */
    public static <T> AnnoBaseProxy<T> getProxyInstance(Class<T> clazz) {
        AnnoMain annoMain = getAnnoMain(clazz);
        Class<? extends AnnoBaseProxy<T>> proxyClazz = (Class<? extends AnnoBaseProxy<T>>) annoMain.annoProxy().value();
        return AnnoBeanUtils.getBean(proxyClazz);
    }

    public static AnnoPreProxy getAnnoPreProxy(Class<?> clazz) {
        List<Class<?>> classes = AnnoUtil.findAllClass(clazz);
        for (Class<?> aClass : classes) {
            AnnoPreProxy annoPreProxy = AnnotationUtil.getAnnotation(aClass, AnnoPreProxy.class);
            if (annoPreProxy == null) {
                continue;
            }
            return annoPreProxy;
        }
        return new AnnoPreProxy() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnoPreProxy.class;
            }

            @Override
            public Class<? extends AnnoPreBaseProxy> value() {
                return AnnoPreDefaultProxy.class;
            }
        };
    }

    /**
     * 获得代理实例
     *
     * @param clazz clazz
     * @return {@link AnnoBaseProxy}<{@link T}>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> AnnoPreBaseProxy<T> getPreProxyInstance(Class<T> clazz) {
        AnnoPreProxy annoPreProxy = AnnoUtil.getAnnoPreProxy(clazz);
        return AnnoBeanUtils.getBean(annoPreProxy.value());
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
     * 获取分类键值
     *
     * @param clazz clazz
     * @return {@link String}
     */
    public static String getCatKey(Class<?> clazz) {
        AnnoMain annotation = AnnotationUtil.getAnnotation(clazz, AnnoMain.class);
        return annotation.annoLeftTree().catKey();
    }

    /**
     * 获取表字段
     *
     * @param clazz 类
     * @return {@link List<String>}
     */
    public static List<String> getTableFields(Class<?> clazz) {
        return getAnnoFields(clazz)
            .stream()
            .map(field -> AnnotationUtil.getAnnotation(field, AnnoField.class))
            .map(AnnoField::tableFieldName)
            .filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }

    /**
     * 获取表字段
     *
     * @param clazz 类
     * @return {@link List<String>}
     */
    public static List<Field> getAnnoFields(Class<?> clazz) {
        List<Field> annoFieldFields = CollUtil.newArrayList();
        List<Class<?>> allClass = AnnoUtil.findAllClass(clazz);
        for (Class<?> aClass : allClass) {
            Field[] declaredFields = ClassUtil.getDeclaredFields(aClass);
            for (Field declaredField : declaredFields) {
                AnnoField annotation = AnnotationUtil.getAnnotation(declaredField, AnnoField.class);
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
        List<Field> declaredFields = AnnoUtil.getAnnoFields(clazz);
        Optional<Field> first = declaredFields.stream().filter(field -> AnnotationUtil.getAnnotation(field, PrimaryKey.class) != null).findFirst();
        return first.map(Field::getName).orElseThrow(() -> new BizException("未找到主键"));
    }


    /**
     * 得到主键字段
     *
     * @param clazz clazz
     * @return {@link String}
     */
    public static Field getPkFieldItem(Class<?> clazz) {
        List<Field> declaredFields = AnnoUtil.getAnnoFields(clazz);
        Optional<Field> first = declaredFields.stream().filter(field -> AnnotationUtil.getAnnotation(field, PrimaryKey.class) != null).findFirst();
        return first.orElseThrow(() -> new BizException("未找到主键"));
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
        List<AnField> anFields = anEntity.getFields();
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