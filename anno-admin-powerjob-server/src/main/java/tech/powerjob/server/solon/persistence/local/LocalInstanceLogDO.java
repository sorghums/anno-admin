package tech.powerjob.server.solon.persistence.local;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.PrimaryKeyModel;
import tech.powerjob.common.enums.LogLevel;


/**
 * 本地的运行时日志
 *
 * @author tjq
 * @since 2020/4/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AnnoMain(name = "本地的运行时日志")
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_local_instance_log")
public class LocalInstanceLogDO extends PrimaryKeyModel {

    @AnnoField(title = "任务实例 id")
    private String instanceId;

    /**
     * 日志时间
     */
    @AnnoField(title = "日志时间")
    private Long logTime;

    /**
     * 日志级别 {@link LogLevel}
     */
    @AnnoField(title = "日志级别")
    private Integer logLevel;

    /**
     * 日志内容
     */
    @AnnoField(title = "日志内容", dataType = AnnoDataType.RICH_TEXT)
    private String logContent;

    /**
     * 机器地址
     */
    @AnnoField(title = "机器地址")
    private String workerAddress;
}
