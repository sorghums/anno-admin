package site.sorghum.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumLabel;
import site.sorghum.anno.anno.annotation.enums.AnnoEnumValue;

@AllArgsConstructor
@Getter
public enum TrueFalseCharEnum {
    TRUE("Y", "是"),
    FALSE("N", "否");

    @AnnoEnumValue
    private final String code;
    
    @AnnoEnumLabel
    private final String name;
}
