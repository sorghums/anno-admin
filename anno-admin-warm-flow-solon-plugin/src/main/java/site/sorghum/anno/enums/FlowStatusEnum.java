package site.sorghum.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumLabel;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumValue;

@AllArgsConstructor
@Getter
public enum FlowStatusEnum {
    //流程状态（0待提交 1审批中 2 审批通过 8已完成 9已退回 10失效）
    WAIT_SUBMIT(0, "待提交"),
    APPROVE(1, "审批中"),
    APPROVE_PASS(2, "审批通过"),
    FINISH(8, "已完成"),
    BACK(9, "已退回"),
    INVALID(10, "失效");
    ;
    @AnnoEnumValue
    private final Integer type;
    @AnnoEnumLabel
    private final String name;
}
