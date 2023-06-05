package site.sorghum.anno.modular.menu.entity.response;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import site.sorghum.anno.modular.menu.entity.model.AnnoMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Anno菜单响应
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnoMenuResponse extends AnnoMenu {
    /**
     * 子节点
     */
    @JSONField(name = "children")
    List<AnnoMenuResponse> children;

}
