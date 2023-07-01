package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

@Data
@EqualsAndHashCode(callSuper = true)
public class Html extends AmisBase {
    {
        setType("html");
    }

    /**
     * html内容
     */
    String html;
}
