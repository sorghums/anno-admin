package site.sorghum.anno.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumLabel;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumValue;

@AllArgsConstructor
@Getter
public enum NodeTypeEnum {
    //0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关
    BEGIN_NODE(0, "开始节点"),
    MIDDLE_NODE(1, "中间节点"),
    END_NODE(2, "结束节点"),
    XOR_GATEWAY(3, "互斥网关"),
    OR_GATEWAY(4, "并行网关");
    @AnnoEnumValue
    private final Integer type;
    @AnnoEnumLabel
    private final String name;
}
