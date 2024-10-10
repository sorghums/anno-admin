package site.sorghum.anno.delaykit.ao;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.delaykit.ao.javacmd.RemoteExeJavaCmdSupplier;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;

@AnnoMain(
    name = "远程任务",
    tableName = "remote_job",
    virtualTable = true,
    canRemove = false
)
@Data
public class RemoteJobAo {
    @AnnoField(
        title = "主键",
        tableFieldName = "id",
        pkField = true,
        show = false
    )
    String id;

    @AnnoField(
        title = "任务名称",
        tableFieldName = "job_name"
    )
    String jobName;

    @AnnoButton(
        name = "执行",
        javaCmd = @AnnoButton.JavaCmd(runSupplier = RemoteExeJavaCmdSupplier.class)
    )
    Object execute;

    @JoinResMap
    HashMap<String, Object> joinResMap;
}
