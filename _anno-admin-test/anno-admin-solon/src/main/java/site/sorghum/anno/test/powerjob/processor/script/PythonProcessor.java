package site.sorghum.anno.test.powerjob.processor.script;

import tech.powerjob.solon.annotation.PowerJob;

/**
 * python processor
 *
 * @author tjq
 * @since 2021/2/7
 */
@PowerJob("PythonProcessor")
public class PythonProcessor extends AbstractScriptProcessor {

    @Override
    protected String getScriptName(Long instanceId) {
        return String.format("python_%d.py", instanceId);
    }

    @Override
    protected String getRunCommand() {
        return "python";
    }
}
