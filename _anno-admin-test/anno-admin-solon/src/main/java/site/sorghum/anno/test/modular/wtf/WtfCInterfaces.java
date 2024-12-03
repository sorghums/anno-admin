package site.sorghum.anno.test.modular.wtf;

import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;

public interface WtfCInterfaces {
    /**
     * 获取位置
     *
     */
    @AnnoField(title = "位置", search = @AnnoSearch, edit = @AnnoEdit)
    default void location(){}
}
