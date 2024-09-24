package site.sorghum.anno._metadata;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;

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

    public static Map<String, AnnoTableButtonImpl> annoJavaCmd2TableButtonMap = new HashMap<>();
}
