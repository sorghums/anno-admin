package site.sorghum.anno.solon.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.noear.wood.Command;
import org.noear.wood.ext.Act1;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/4 12:59
 */
@Slf4j
public class WoodSqlLogInterceptor implements Act1<Command> {

    List<String> excludeSqlList = List.of("select id from pj_app_info where current_server = ?", "UPDATE `pj_server_info` SET `update_time`=? WHERE `ip` = ? ");

    @Override
    public void run(Command cmd) {
        if (excludeSqlList.contains(cmd.text)) {
            return;
        }
        log.debug("===[Wood] sql: {}", cmd.text);
        log.debug("===[Wood] var: {}", cmd.paramMap());
    }


}
