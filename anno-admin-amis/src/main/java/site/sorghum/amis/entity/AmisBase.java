package site.sorghum.amis.entity;

import lombok.Data;

import java.util.Map;

@Data
public class AmisBase {
    /**
     * amis组件类型
     */
    private String type;

    /**
     * amis组件id
     */
    private String id;

    /**
     * 外层 Dom 的类名
     */
    private String className;

    /**
     * 事件
     */
    Map<String,Object> onEvent;

    /**
     * 是否隐藏
     */
    Boolean hidden;
}
