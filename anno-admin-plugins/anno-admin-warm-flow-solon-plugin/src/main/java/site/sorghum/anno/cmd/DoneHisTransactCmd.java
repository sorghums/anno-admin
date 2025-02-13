//package site.sorghum.anno.cmd;
//
//import lombok.extern.slf4j.Slf4j;
//import org.dromara.warm.flow.core.dto.FlowParams;
//import org.dromara.warm.flow.core.entity.Node;
//import org.dromara.warm.flow.core.service.InsService;
//import org.dromara.warm.flow.core.service.NodeService;
//import org.dromara.warm.flow.core.service.TaskService;
//import org.noear.solon.annotation.Component;
//import org.noear.solon.annotation.Inject;
//import site.sorghum.anno._common.entity.CommonParam;
//import site.sorghum.anno.ao.WaitFlowTaskAo;
//import site.sorghum.anno.auth.AnnoStpUtil;
//import site.sorghum.anno.form.TransactForm;
//
//import java.util.List;
//
//@Component
//@Slf4j
//public class DoneHisTransactCmd implements FlowBaseJavaCmdSupplier {
//    @Inject
//    InsService insService;
//
//    @Inject
//    TaskService taskService;
//
//    @Inject
//    NodeService nodeService;
//
//    @Override
//    public String run(CommonParam param) {
//        return parcel(()->{
//            WaitFlowTaskAo waitFlowTaskAo = param.toT(WaitFlowTaskAo.class);
//            TransactForm transactForm = param.getExtraInput().toT(TransactForm.class);
//            log.info("办理成功:{}", param);
//            String skipType = transactForm.getStatus().equals(1) ? "PASS" : "REJECT";
//            List<Node> nextNodeList = nodeService.getNextNodeList(waitFlowTaskAo.getDefinitionId(), waitFlowTaskAo.getNodeCode(), "", skipType, null);
//            taskService.skip(waitFlowTaskAo.getId(),
//                new FlowParams().
//                    message(transactForm.getMessage()).
//                    handler(AnnoStpUtil.getLoginIdAsString()).
//                    permissionFlag(List.of("1")).
//                    skipType(skipType));
//            return defaultMsgSuccess("办理成功");
//        });
//    }
//
//}
