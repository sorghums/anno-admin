package site.sorghum.anno.ddl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.wood.WoodConfig;

/**
 * @author songyinyin
 * @since 2023/7/4 12:02
 */
@SolonMain
@Slf4j
public class TestApp {
  public static void main(String[] args) {
    Solon.start(TestApp.class, args, app ->{
      //执行后打印sql
      WoodConfig.onExecuteAft(cmd -> {
        log.debug("===[Wood] sql: {}",cmd.text);
        log.debug("===[Wood] var: {}",cmd.paramMap());
      });
    });
  }
}
