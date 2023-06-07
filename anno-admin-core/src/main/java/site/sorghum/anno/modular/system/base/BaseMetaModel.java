package site.sorghum.anno.modular.system.base;

import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPreProxy;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 雪花模型
 *
 * @author Sorghum
 * @since 2023/03/08
 */
@Data
@AnnoPreProxy(BaseAnnoPreProxy.class)
@AnnoRemove(removeType = 1)
public class BaseMetaModel implements Serializable {
    @AnnoField(isId = true, title = "主键", tableFieldName = "id", show = false)
    protected String id;

    @AnnoField(title = "创建人", tableFieldName = "create_by", show = false)
    private String createBy;

    @AnnoField(title = "创建时间", tableFieldName = "create_time", dataType = AnnoDataType.DATETIME, show = false)
    private LocalDateTime createTime;

    @AnnoField(title = "更新人", tableFieldName = "update_by", show = false)
    private String updateBy;

    @AnnoField(title = "更新时间", tableFieldName = "update_time", dataType = AnnoDataType.DATETIME, show = false)
    private LocalDateTime updateTime;

    @AnnoField(title = "删除标识", tableFieldName = "del_flag", show = false)
    private Integer delFlag;
}
