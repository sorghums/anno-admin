package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.plugin.service.AnSqlService;
import site.sorghum.anno.plugin.tpl.AnnoSqlDetailTplRender;
import site.sorghum.anno.suppose.model.PrimaryKeyModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(
    name = "数据库脚本管理",
    annoPermission = @AnnoPermission(
        enable = true,
        baseCode = "an_sql",
        baseCodeTranslate = "数据库脚本管理"
    ),
    canRemove = false
)
@Table("an_sql")
public class AnSql extends PrimaryKeyModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数据库脚本版本
     */
    @AnnoField(
        title = "数据库脚本版本",
        tableFieldName = "version"
    )
    private String version;

    /**
     * 脚本运行状态，0-未执行，1-成功，2-失败
     */
    @AnnoField(
        title = "脚本运行状态",
        tableFieldName = "state",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            value = {
                @AnnoOptionType.OptionData(label = "未执行", value = "0"),
                @AnnoOptionType.OptionData(label = "成功", value = "1"),
                @AnnoOptionType.OptionData(label = "失败", value = "2")
            }
        )
    )
    private Integer state;

    /**
     * 运行时间
     */
    @AnnoField(
        title = "运行时间",
        tableFieldName = "run_time"
    )
    private Date runTime;

    /**
     * 错误日志
     */
    @AnnoField(
        title = "错误日志",
        tableFieldName = "error_log",
        dataType = AnnoDataType.TEXT_AREA
    )
    private String errorLog;

    @AnnoButton(
        name = "手动执行",
        javaCmd = @AnnoButton.JavaCmd(
            beanClass = AnSqlService.class,
            methodName = "runSql"
        ),
        permissionCode = "runSql"
    )
    private Object runButton;

    @AnnoButton(
        name = "查看SQL内容",
        annoTpl = @AnnoTpl(
            tplClazz = AnnoSqlDetailTplRender.class
        ),
        permissionCode = "sqlContent"
    )
    private Object sqlContentButton;

}
