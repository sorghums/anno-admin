package site.sorghum.anno.anno.entity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Anno 树
 *
 * @author sorghum
 * @since 2023/05/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnoTreeDTO<T> {
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
     * 标签 alias
     */
    String title;

    /**
     * 值
     */
    T value;

    /**
     * 值 alias
     */
    T key;

    /**
     * 子节点
     */
    List<AnnoTreeDTO<T>> children;
}
