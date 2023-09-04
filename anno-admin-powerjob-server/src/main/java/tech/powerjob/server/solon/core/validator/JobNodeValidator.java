package tech.powerjob.server.solon.core.validator;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.core.workflow.algorithm.WorkflowDAG;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowNodeInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;
import tech.powerjob.common.enums.WorkflowNodeType;
import tech.powerjob.common.exception.PowerJobException;

/**
 * @author Echo009
 * @since 2021/12/14
 */
@Component
@Slf4j
public class JobNodeValidator implements NodeValidator {

    @Db
    private JobInfoRepository jobInfoRepository;

    @Override
    public void complexValidate(WorkflowNodeInfoDO node, WorkflowDAG dag) {
        // do nothing
    }

    @Override
    public void simpleValidate(WorkflowNodeInfoDO node) {
        // 判断对应的任务是否存在
        JobInfoDO job = jobInfoRepository.findById(String.valueOf(node.getJobId()))
                .orElseThrow(() -> new PowerJobException("Illegal job node,specified job is not exist,node name : " + node.getNodeName()));

        if (job.getStatus() == SwitchableStatus.DELETED.getV()) {
            throw new PowerJobException("Illegal job node,specified job has been deleted,node name : " + node.getNodeName());
        }
    }

    @Override
    public WorkflowNodeType matchingType() {
        return WorkflowNodeType.JOB;
    }
}
