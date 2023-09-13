package site.sorghum.anno.test.powerjob.processor.script;

import tech.powerjob.solon.annotation.PowerJob;

import java.nio.charset.Charset;

/**
 * CMDProcessor
 *
 * @author fddc
 * @since 2021/5/14
 */
@PowerJob("CMDProcessor")
public class CMDProcessor extends AbstractScriptProcessor {

    @Override
    protected String getScriptName(Long instanceId) {
        return String.format("cmd_%d.bat", instanceId);
    }

    @Override
    protected String getRunCommand() {
        return "cmd.exe";
    }

    @Override
    protected Charset getCharset() {
        return Charset.defaultCharset();
    }
}
