package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * 年份java cmd
 *
 * @author Sorghum
 * @since 2023/12/08
 */
@Data
public class AnnoJavaCmd {

    /**
     * 多对多映射值
     */
    public static Map<String, AnnoJavaCmd> annoJavCmdMap = new HashMap<>();

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "AnnUser:2321321")
    String id;

    /**
     * 运行供应商
     */
    @ApiModelProperty(value = "JavaCmd：supplier类", example = "JavaCmdSupplier")
    private Class<? extends JavaCmdSupplier> runSupplier;

    /**
     * 权限码
     */
    @ApiModelProperty(value = "权限码")
    private String permissionCode;

}
