package site.sorghum.amis.entity.layout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.ArrayList;
import java.util.List;

/**
 * 分栏容器
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tabs extends AmisBase {
    {
        setType("tabs");
        setClassName("p-l m-l");
    }

    /**
     * 标签页
     */
    List<Tab> tabs = new ArrayList<>();

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Tab extends AmisBase{
        /**
         * 标题
         */
        String title;

        /**
         * 内容
         */
        List<AmisBase> body;

        {
            setClassName("b");
        }
    }
}
