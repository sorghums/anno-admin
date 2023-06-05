package site.sorghum.anno.modular.message.entity.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Anno菜单
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@NoArgsConstructor
@Data
public class AnnoMessage {

    private Integer id;

    private String title;

    private String avatar;

    private String context;

    private String form;

    private String time;
}
