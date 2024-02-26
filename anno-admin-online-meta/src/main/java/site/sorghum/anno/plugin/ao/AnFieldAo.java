package site.sorghum.anno.plugin.ao;

import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;

/**
 * An字段ao
 *
 * @author Sorghum
 * @since 2024/02/26
 */
@AnnoMain(name = "字段管理", tableName = "an_field_ao")
public class AnFieldAo extends AnField {
    @Override
    public String getFieldName() {
        return super.getFieldName();
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public String getTableFieldName() {
        return super.getTableFieldName();
    }

    @Override
    public boolean isPrimaryKey() {
        return super.isPrimaryKey();
    }

    @Override
    public Class<?> getFieldType() {
        return super.getFieldType();
    }
}
