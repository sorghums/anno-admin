package site.sorghum.anno.modular.menu.entity.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.modular.menu.entity.model.SysAnnoMenu;

import java.util.List;

/**
 * Anno菜单响应
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAnnoMenuResponse extends SysAnnoMenu {
    /**
     * 子节点
     */
    @JSONField(name = "children")
    List<SysAnnoMenuResponse> children;

}
