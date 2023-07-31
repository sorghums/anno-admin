package site.sorghum.anno._common.config;

import lombok.Data;

/**
 * anno 配置项
 *
 * @author songyinyin
 * @since 2023/7/9 17:47
 */
@Data
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

    /**
     * 是否开启验证码，默认为 true 开启
     */
    private boolean captchaEnable = true;
    /**
     * 本地文件存储路径
     */
    private String localFilePath = "./anLocal/";
}
