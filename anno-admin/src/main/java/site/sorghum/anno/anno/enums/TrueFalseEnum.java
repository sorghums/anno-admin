package site.sorghum.anno.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumLabel;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumValue;
import site.sorghum.anno.anno.entity.common.TagEnumLabel;

@AllArgsConstructor
@Getter
public enum TrueFalseEnum {
    TRUE(1, TagEnumLabel.builder().value("是").color("green").build()),
    FALSE(0, TagEnumLabel.builder().value("否").color("red").build());

    @AnnoEnumValue
    private final Integer value;

    @AnnoEnumLabel
    private final TagEnumLabel name;
}
