package site.sorghum.anno._common.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
     * 是否开启验证码，默认为 true 开启
     */
    private boolean captchaEnable = false;
    /**
     * 本地文件存储路径
     */
    private String localFilePath = "./anLocal/";

    /**
     * 接口服务地址
     */
    private String apiServerUrl = "http://localhost:" + System.getProperty("server.port");

    /**
     * 是否打印执行的 sql
     */
    private boolean showSql = true;

    /**
     * 忽略打印日志的表
     */
    private List<String> skipTable = new ArrayList<>();

    /**
     * 忽略打印日志的请求路径
     *
     * @see cn.hutool.core.text.AntPathMatcher
     */
    private List<String> skipPathPattern = new ArrayList<>();

    /**
     * 打印详细日志的阈值（毫秒），默认为超过 500 毫秒，就会打印出详细日志
     */
    private long detailLogThreshold = 500;

    /**
     * 系统标题
     */
    private String platformTitle = "AnnoAdmin快速开发";

    /**
     * 系统描述
     */
    private String platformDesc = "零前端代码，注解驱动";

    /**
     * 系统 Logo
     */
    private String platformLogo = "";
}
