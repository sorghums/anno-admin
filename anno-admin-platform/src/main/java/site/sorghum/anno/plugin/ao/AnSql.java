package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.PrimaryKeyModel;
import site.sorghum.anno.plugin.javacmd.RunSqlJavaCmdSupplier;
import site.sorghum.anno.plugin.tpl.AnnoSqlDetailTplRender;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(
    name = "数据库脚本管理",
    tableName = "an_sql",
    annoPermission = @AnnoPermission(
        enable = true,
        baseCode = "an_sql",
        baseCodeTranslate = "数据库脚本管理"
    ),
    canRemove = true
)
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

    /**
     * SQL内容
     */
    @AnnoField(
        title = "SQL内容",
        tableFieldName = "sql_content",
        dataType = AnnoDataType.TEXT_AREA,
        show = false
    )
    private String sqlContent;

    /**
     * SQL MD5
     */
    @AnnoField(
        title = "SQL MD5",
        tableFieldName = "sql_md5",
        fieldSize = 64,
        show = false
    )
    private String sqlMd5;

    @AnnoButton(
        name = "手动执行",
        javaCmd = @AnnoButton.JavaCmd(
            runSupplier = RunSqlJavaCmdSupplier.class
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
