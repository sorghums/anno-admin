package site.sorghum.anno.common.config;

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
@Inject(value = "${anno-admin}", required = false)
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

    /**
     * 前端主题样式：默认为 antd，可选项为 antd、sdk、ang、cxd、dark
     */
    private String theme = "antd";
}
