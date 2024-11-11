package site.sorghum.anno.anno.entity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagEnumLabel {
    /**
     * 标签值
     */
    String value;

    /**
     * 标签颜色
     */
    String color;
}
