package tech.powerjob.server.solon.persistence.remote.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.suppose.model.BaseMetaModel;
import tech.powerjob.common.enums.WorkflowNodeType;

/**
 * 工作流节点信息
 * 记录了工作流中的任务节点个性化的配置信息
 *
 * @author Echo009
 * @since 2021/1/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "工作流节点信息")
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_workflow_node_info")
public class WorkflowNodeInfoDO extends BaseMetaModel {


    @AnnoField(title = "所属应用ID")
    private String appId;

    @AnnoField(title = "工作流ID")
    private String workflowId;
    /**
     * 节点类型 {@link WorkflowNodeType}
     */
    @AnnoField(title = "节点类型")
    private Integer type;
    /**
     * 任务 ID
     * 对于嵌套工作流类型的节点而言，这里存储是工作流 ID
     */
    @AnnoField(title = "任务ID")
    private String jobId;
    /**
     * 节点名称，默认为对应的任务名称
     */
    @AnnoField(title = "节点名称")
    private String nodeName;
    /**
     * 节点参数
     */
    @AnnoField(title = "节点参数", dataType = AnnoDataType.CODE_EDITOR)
    private String nodeParams;
    /**
     * 是否启用
     */
    @AnnoField(title = "是否启用")
    private Boolean enable;
    /**
     * 是否允许失败跳过
     */
    @AnnoField(title = "是否允许失败跳过")
    private Boolean skipWhenFailed;

    /**
     * 节点的额外信息
     */
    @AnnoField(title = "节点的额外信息", dataType = AnnoDataType.CODE_EDITOR)
    private String extra;

}
