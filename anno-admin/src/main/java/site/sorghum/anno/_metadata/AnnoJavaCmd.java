package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
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

    public static Map<String, AnnoButtonImpl.JavaCmdImpl> annoJavCmdMap = new HashMap<>();

    public static Map<String, AnnoButtonImpl> annoJavaCmd2ButtonMap = new HashMap<>();
}
