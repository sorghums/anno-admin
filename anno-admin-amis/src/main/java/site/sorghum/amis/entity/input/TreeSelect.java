package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 树选择
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeSelect extends InputTree{
    {
        setType("tree-select");
    }

    /**
     * 是否隐藏选择框中已选择节点的路径 label 信息
     */
    boolean hideNodePathLabel = false;

    /**
     * 只允许选择叶子节点
     */
    boolean onlyLeaf = false;

}
