package site.sorghum.anno.test.powerjob.processor;

import org.apache.commons.lang3.StringUtils;
import tech.powerjob.worker.core.processor.TaskContext;

/**
 * CommonUtils
 *
 * @author tjq
 * @since 2021/2/1
 */
public class CommonUtils {

    private CommonUtils() {

    }

    public static String parseParams(TaskContext context) {
        // 工作流中的总是优先使用 jobParams
        if (context.getWorkflowContext().getWfInstanceId() != null) {
            return context.getJobParams();
        }
        if (StringUtils.isNotEmpty(context.getInstanceParams())) {
            return context.getInstanceParams();
        }
        return context.getJobParams();
    }
}
