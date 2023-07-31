package site.sorghum.anno.anno.entity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno._common.util.JSONUtil;

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
     * 值
     */
    T value;

    /**
     * 子节点
     */
    List<AnnoTreeDTO<T>> children;

    /**
     * 转换为 Options
     * @param annoTrees AnnoTreeDto
     * @return {@link List<Options.Option>}
     */
    public static List<Options.Option> toOptions(List<AnnoTreeDTO<String>> annoTrees) {
        // 转换为 Options
        String jsonString = JSONUtil.toJsonString(annoTrees);
        return JSONUtil.parseArray(jsonString, Options.Option.class);
    }
}
