package site.sorghum.anno.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.orm.entity.FlowNode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdLongSupplier;
import site.sorghum.anno.anno.proxy.field.ZeroFiledStringBaseSupplier;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AnnoMain(name = "流程定义",
    tableName = "flow_definition",
    annoPermission = @AnnoPermission(baseCode = "flow_definition", baseCodeTranslate = "流程定义"),
    autoMaintainTable = false
)
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowNodeAo extends FlowNode {
    @Override
    @AnnoField(title = "主键", tableFieldName = "id", show = false, fieldSize = 32, insertWhenNullSet = SnowIdLongSupplier.class, pkField = true)
    public Long getId() {
        return super.getId();
    }

    @Override
    @AnnoField(title = "创建时间", tableFieldName = "create_time", dataType = AnnoDataType.DATETIME, show = false)
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    @AnnoField(title = "更新时间", tableFieldName = "update_time", dataType = AnnoDataType.DATETIME, show = false)
    public Date getUpdateTime() {
        return super.getUpdateTime();
    }

    @Override
    public String getTenantId() {
        return super.getTenantId();
    }

    @Override
    @AnnoField(title = "删除标识", tableFieldName = "del_flag",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "已删除", value = "1"),
            @AnnoOptionType.OptionData(label = "正常", value = "0")
        }), show = false, fieldSize = 1, insertWhenNullSet = ZeroFiledStringBaseSupplier.class)
    public String getDelFlag() {
        return super.getDelFlag();
    }

    @Override
    //节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）
    @AnnoField(title = "节点类型", tableFieldName = "node_type")
    public Integer getNodeType() {
        return super.getNodeType();
    }



    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
