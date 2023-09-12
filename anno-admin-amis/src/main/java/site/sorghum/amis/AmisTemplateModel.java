package site.sorghum.amis;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmisTemplateModel {
    /**
     * amis模板中的key
     */
    String key;

    /**
     * amis模板中的value
     */
    Object value;
}
