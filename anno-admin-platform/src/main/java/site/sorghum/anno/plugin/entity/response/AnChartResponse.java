package site.sorghum.anno.plugin.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.enums.AnnoChartType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnChartResponse<T> {

    String name;

    AnnoChartType type;

    T value;

    int order;
}
