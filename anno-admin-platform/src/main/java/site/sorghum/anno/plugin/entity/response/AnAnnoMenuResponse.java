package site.sorghum.anno.plugin.entity.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 * Anno菜单响应
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnAnnoMenuResponse extends AnAnnoMenu {
    /**
     * 子节点
     */
    List<AnAnnoMenuResponse> children;

}
