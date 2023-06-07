package site.sorghum.anno.modular.anno.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import org.noear.solon.Solon;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPreProxy;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDto;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.anno.proxy.AnnoPreDefaultProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
        return Solon.context().getBean(proxyClazz);
    }

    public static AnnoPreProxy getAnnoPreProxy(Class<?> clazz) {
        return AnnotationUtil.getAnnotation(clazz, AnnoPreProxy.class);
    }

    /**
     * 获得代理实例
     *
     * @param clazz clazz
     * @return {@link AnnoBaseProxy}<{@link T}>
     */
    public static <T> AnnoPreBaseProxy<T> getPreProxyInstance(Class<T> clazz) {
        List<Class<?>> classes = AnnoUtil.findAllClass(clazz);
        for (Class<?> aClass : classes) {
            AnnoPreProxy annoPreProxy = AnnoUtil.getAnnoPreProxy(aClass);
            if (annoPreProxy == null) {
                continue;
            }
            Class<? extends AnnoPreBaseProxy> value = annoPreProxy.value();
            return Solon.context().getBean(value);
        }
        return Solon.context().getBean(AnnoPreDefaultProxy.class);
    }

    public static AnnoRemove getAnnoRemove(Class<?> clazz) {
        List<Class<?>> allClass = findAllClass(clazz);
        for (Class<?> aClass : allClass) {
            AnnoRemove annoRemove = AnnotationUtil.getAnnotation(aClass, AnnoRemove.class);
            if (annoRemove != null) {
                return annoRemove;
            }
        }
        return new AnnoRemove(){
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
        return AnnotationUtil.getAnnotationValue(clazz, AnnoMain.class, "tableName");
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
        return getAnnoFields(clazz).stream().map(field -> AnnotationUtil.getAnnotation(field, AnnoField.class)).map(AnnoField::tableFieldName).collect(Collectors.toList());
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
        Optional<Field> first = declaredFields.stream().filter(field -> AnnotationUtil.getAnnotation(field, AnnoField.class).isId()).findFirst();
        return first.map(Field::getName).orElseThrow(() -> new BizException("未找到主键"));
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

    public static <T> List<AnnoTreeDto<String>> buildAnnoTree(List<T> datas,
                                                              String label,
                                                              String key,
                                                              String parentKey) {
        List<AnnoTreeDto<String>> annoTreeDtos = list2AnnoTreeNode(datas, label, key, parentKey);
        return listToTree(annoTreeDtos);
    }

    public static List<AnnoTreeDto<String>> listToTree(List<AnnoTreeDto<String>> list) {
        Map<String, AnnoTreeDto<String>> map = new HashMap<>();
        List<AnnoTreeDto<String>> roots = new ArrayList<>();
        for (AnnoTreeDto<String> node : list) {
            map.put(node.getId(), node);
        }
        for (AnnoTreeDto<String> node : list) {
            if (isRootNode(node.getParentId())) {
                roots.add(node);
            } else {
                AnnoTreeDto<String> parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    private static <T> List<AnnoTreeDto<String>> list2AnnoTreeNode(List<T> datas,
                                                                   String label,
                                                                   String key,
                                                                   String parentKey) {
        return datas.stream().map(
                d -> {
                    AnnoTreeDto<String> annoTreeDto = new AnnoTreeDto<>();
                    annoTreeDto.setId(simpleToString(ReflectUtil.getFieldValue(d, key)));
                    annoTreeDto.setLabel(simpleToString(ReflectUtil.getFieldValue(d, label)));
                    annoTreeDto.setValue(simpleToString(ReflectUtil.getFieldValue(d, key)));
                    annoTreeDto.setParentId(simpleToString(ReflectUtil.getFieldValue(d, parentKey)));
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
}
