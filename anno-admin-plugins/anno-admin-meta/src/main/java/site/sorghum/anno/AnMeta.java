package site.sorghum.anno;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno.anno.annotation.clazz.AnnoMainImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoFieldImpl;
import site.sorghum.anno.annotation.AnnoIgnore;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnMeta extends AnnoMainImpl {

    @AnnoIgnore
    private String entityName;
    @AnnoIgnore
    private String extend;
    @AnnoIgnore
    private List<Column> columns;
    @AnnoIgnore
    private List<AnnoButtonImpl> columnButtons;


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Column extends AnnoFieldImpl {
        @AnnoIgnore
        private String javaName;
        @AnnoIgnore
        private Class<?> javaType;
    }
}
