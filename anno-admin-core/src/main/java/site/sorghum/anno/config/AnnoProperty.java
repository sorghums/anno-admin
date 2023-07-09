package site.sorghum.anno.config;

import lombok.Data;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

/**
 * anno 配置项
 *
 * @author songyinyin
 * @since 2023/7/9 17:47
 */
@Data
@Inject(value = "${anno}", required = false)
@Configuration
public class AnnoProperty {

  /**
   * 是否维护预置数据（添加和升级），默认为 ture
   */
  private Boolean isAutoMaintainInitData = true;

  /**
   * 是否自动维护表结构，默认为 true
   */
  private Boolean isAutoMaintainTable = true;
}
