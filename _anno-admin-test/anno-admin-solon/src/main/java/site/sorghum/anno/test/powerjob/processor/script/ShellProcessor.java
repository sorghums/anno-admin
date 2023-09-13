package site.sorghum.anno.test.powerjob.processor.script;

import tech.powerjob.solon.annotation.PowerJob;

/**
 * shell processor
 *
 * @author tjq
 * @since 2021/2/7
 */
@PowerJob("ShellProcessor")
public class ShellProcessor extends AbstractScriptProcessor {

    @Override
    protected String getScriptName(Long instanceId) {
        return String.format("shell_%d.sh", instanceId);
    }

    @Override
    protected String getRunCommand() {
        return SH_SHELL;
    }
}
