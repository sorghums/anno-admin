package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.PrimaryKeyModel;
import site.sorghum.anno.db.QueryType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(
    name = "登录日志",
    tableName = "an_login_log",
    annoPermission = @AnnoPermission(
        enable = true,
        baseCode = "an_login_log",
        baseCodeTranslate = "登录日志"
    ),
    annoOrder = @AnnoOrder(
        orderType = "desc",
        orderValue = "latestTime"
    ),
    canRemove = false
)
public class AnLoginLog extends PrimaryKeyModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @AnnoField(
        title = "用户",
        tableFieldName = "user_id",
        dataType = AnnoDataType.CLASS_OPTIONS,
        search = @AnnoSearch(queryType = QueryType.EQ),
        optionType = @AnnoOptionType(
            optionAnno = @AnnoOptionType.OptionAnnoClass(
                annoClass = AnUser.class
            )
        )
    )
    private String userId;

    /**
     * 最后登录IP
     */
    @AnnoField(
        title = "最后登录IP",
        tableFieldName = "latest_ip"
    )
    private String latestIp;

    /**
     * 最后登录时间
     */
    @AnnoField(
        title = "最后登录时间",
        tableFieldName = "latest_time"
    )
    private Date latestTime;

    /**
     * 浏览器
     */
    @AnnoField(
        title = "浏览器",
        tableFieldName = "browser"
    )
    private String browser;

    /**
     * 系统
     */
    @AnnoField(
        title = "系统",
        tableFieldName = "os"
    )
    private String os;

    /**
     * 设备
     */
    @AnnoField(
        title = "设备",
        tableFieldName = "device",
        search = @AnnoSearch(),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            value = {
                @AnnoOptionType.OptionData(label = "桌面端", value = "computer"),
                @AnnoOptionType.OptionData(label = "移动端", value = "mobile")
            }
        )
    )
    private String device;
}
