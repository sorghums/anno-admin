package site.sorghum.anno._metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被{@link AnnoMain}标识的 entity
 *
 * @author songyinyin
 * @since 2023/7/9 19:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnEntity extends AnMeta {

    /**
     * field映射
     */
    Map<String,AnField> fieldMap = new HashMap<>();

    /**
     * 获取数据库字段（排除虚拟列字段）
     *
     * @return 返回一个包含所有非虚拟列字段的AnField对象列表
     */
    public List<AnField> getDbAnFields() {
        return getColumns().stream().filter(anField -> !anField.isVirtualColumn()).collect(Collectors.toList());
    }

    public AnField getPkField() {
        return getColumns().stream().filter(AnField::isPkField).findFirst().orElseThrow(() -> new RuntimeException("没有主键"));
    }

    public List<AnField> getFields() {
        return getColumns();
    }

    public AnField getField(String fieldName) {
        return getFieldMap().get(fieldName);
    }

    private synchronized void initFieldMap() {
        getColumns().forEach(anField -> this.fieldMap.put(anField.getJavaName(), anField));
    }

    public Map<String, AnField> getFieldMap() {
        initFieldMap();
        return fieldMap;
    }
}
