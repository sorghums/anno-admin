package site.sorghum.anno.anno.option;

import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 选项数据供应商
 *
 * @author Sorghum
 * @since 2024/06/21
 */
public interface OptionDataSupplier {

    /**
     * 类映射
     */
    public static final Map<String, Class<? extends OptionDataSupplier>> CLASS_MAP = new ConcurrentHashMap<>();

    /**
     * 初始化方法，将传入的OptionDataSupplier的类型的Class对象存储到CLASS_MAP中
     *
     */
    default void init() {
        Class<? extends OptionDataSupplier> clazz = this.getClass();
        CLASS_MAP.put(clazz.getSimpleName()
            , clazz);
    }

    ;

    /**
     * 获取选项数据列表
     *
     * @return 包含选项数据的List集合
     */
    List<AnnoOptionTypeImpl.OptionDataImpl> getOptionDataList();
}
