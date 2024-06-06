package site.sorghum.anno.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumLabel;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumValue;

@AllArgsConstructor
@Getter
public enum TrueFalseEnum {
    TRUE(1, "是"),
    FALSE(0, "否");

    @AnnoEnumValue
    private final Integer code;
    
    @AnnoEnumLabel
    private final String name;
}
