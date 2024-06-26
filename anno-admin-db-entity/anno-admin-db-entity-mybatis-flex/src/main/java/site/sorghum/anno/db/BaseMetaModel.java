package site.sorghum.anno.db;


import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.ZeroFiledBaseSupplier;

import java.time.LocalDateTime;

/**
 * 雪花模型
 *
 * @author Sorghum
 * @since 2023/03/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoRemove(removeType = 1)
public class BaseMetaModel extends PrimaryKeyModel {

    @AnnoField(title = "创建人", tableFieldName = "create_by", show = false, fieldSize = 32)
    @Column(value = "create_by")
    private String createBy;

    @AnnoField(title = "创建时间", tableFieldName = "create_time", dataType = AnnoDataType.DATETIME, show = false)
    @Column(value = "create_time")
    private LocalDateTime createTime;

    @AnnoField(title = "更新人", tableFieldName = "update_by", show = false, fieldSize = 32)
    @Column(value = "update_by")
    private String updateBy;

    @AnnoField(title = "更新时间", tableFieldName = "update_time", dataType = AnnoDataType.DATETIME, show = false)
    @Column(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 状态 1 正常 0 封禁
     */
    @AnnoField(title = "删除标识", tableFieldName = "del_flag",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "已删除", value = "1"),
            @AnnoOptionType.OptionData(label = "正常", value = "0")
        }), show = false, fieldSize = 1, insertWhenNullSet = ZeroFiledBaseSupplier.class)
    @Column(isLogicDelete = true)
    private Integer delFlag;
}
