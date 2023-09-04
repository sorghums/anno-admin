package tech.powerjob.server.solon.core.service;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import tech.powerjob.server.solon.core.validator.NodeValidator;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAG;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowNodeInfoDO;
import tech.powerjob.common.enums.WorkflowNodeType;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Echo009
 * @since 2021/12/14
 */
@Component
@Slf4j
public class NodeValidateService implements LifecycleBean {

    private Map<WorkflowNodeType, NodeValidator> nodeValidatorMap;

    @Override
    public void start() {
        nodeValidatorMap = new EnumMap<>(WorkflowNodeType.class);
        Solon.context().getBeansOfType(NodeValidator.class).forEach(e -> nodeValidatorMap.put(e.matchingType(), e));
    }


    public void complexValidate(WorkflowNodeInfoDO node, WorkflowDAG dag) {
        NodeValidator nodeValidator = getNodeValidator(node);
        if (nodeValidator == null) {
            // 默认不需要校验
            return;
        }
        nodeValidator.complexValidate(node, dag);
    }

    public void simpleValidate(WorkflowNodeInfoDO node) {
        NodeValidator nodeValidator = getNodeValidator(node);
        if (nodeValidator == null) {
            // 默认不需要校验
            return;
        }
        nodeValidator.simpleValidate(node);
    }

    private NodeValidator getNodeValidator(WorkflowNodeInfoDO node) {
        Integer nodeTypeCode = node.getType();
        if (nodeTypeCode == null) {
            // 前向兼容，默认为 任务节点
            return nodeValidatorMap.get(WorkflowNodeType.JOB);
        }
        return nodeValidatorMap.get(WorkflowNodeType.of(nodeTypeCode));
    }
}
