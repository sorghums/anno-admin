package tech.powerjob.server.solon.persistence.remote.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.suppose.model.BaseMetaModel;

/**
 * 数据库锁
 *
 * @author tjq
 * @since 2020/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "数据库锁")
@NoArgsConstructor
@Table("pj_oms_lock")
public class OmsLockDO extends BaseMetaModel {

    @AnnoField(title = "锁名称")
    private String lockName;

    @AnnoField(title = "锁拥有者ip")
    private String ownerIP;
    /**
     * 最长持有锁的时间
     */
    @AnnoField(title = "最长持有锁的时间")
    private Long maxLockTime;

    public OmsLockDO(String lockName, String ownerIP, Long maxLockTime) {
        this.lockName = lockName;
        this.ownerIP = ownerIP;
        this.maxLockTime = maxLockTime;
    }
}
