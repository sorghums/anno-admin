package site.sorghum.anno.db;


import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.interfaces.BaseMetaModelInterface;

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
public class BaseMetaModel extends PrimaryKeyModel implements BaseMetaModelInterface {

    @Column(value = "create_by")
    private String createBy;

    @Column(value = "create_time")
    private LocalDateTime createTime;

    @Column(value = "update_by")
    private String updateBy;

    @Column(value = "update_time")
    private LocalDateTime updateTime;

    @Column(isLogicDelete = true)
    private Integer delFlag;
}
