package site.sorghum.anno.modular.anno.entity.common;

import lombok.Data;

import java.util.List;

/**
 * Anno 树
 *
 * @author sorghum
 * @since 2023/05/21
 */
@Data
public class AnnoTreeDto<T> {
    /**
     * id
     */
    String id;
    /**
     * 父id
     */
    String parentId;
    /**
     * 标签
     */
    String label;

    /**
     * 值
     */
    T value;

    /**
     * 子节点
     */
    List<AnnoTreeDto<T>> children;

}
