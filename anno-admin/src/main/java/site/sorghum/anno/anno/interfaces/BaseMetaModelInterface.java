package site.sorghum.anno.anno.interfaces;

import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoSql;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.ZeroFiledBaseSupplier;


public interface BaseMetaModelInterface {
    @AnnoField(
        title = "创建人",
        tableFieldName = "create_by",
        show = false,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(anSql = @AnnoSql(sql = """
                select id, name as label from an_user
            """)),
        fieldSize = 32)
    default void createBy() {
    }

    ;

    @AnnoField(title = "创建时间", tableFieldName = "create_time", dataType = AnnoDataType.DATETIME, show = false)
    default void createTime() {
    }

    ;

    @AnnoField(title = "更新人",
        tableFieldName = "update_by",
        show = false,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(anSql = @AnnoSql(sql = """
                select id, name as label from an_user
            """)),
        fieldSize = 32)
    default void updateBy() {
    }

    ;

    @AnnoField(title = "更新时间", tableFieldName = "update_time", dataType = AnnoDataType.DATETIME, show = false)
    default void updateTime() {
    }

    ;

    /**
     * 状态 1 正常 0 封禁
     */
    @AnnoField(title = "删除标识", tableFieldName = "del_flag",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "已删除", value = "1"),
            @AnnoOptionType.OptionData(label = "正常", value = "0")
        }), show = false, fieldSize = 1, insertWhenNullSet = ZeroFiledBaseSupplier.class)
    default void delFlag() {
    }

    ;
}
