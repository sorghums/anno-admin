package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;

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
    public static Map<String, AnnoMtm> annoJavCmdMap = new HashMap<>();

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "AnnUser:2321321")
    String id;

    /**
     * JavaCmd：bean类
     *
     * @see AnnoTableButton.JavaCmd#beanClass()
     */
    @ApiModelProperty(value = "JavaCmd：bean类", example = "b12345")
    private Class<?> javaCmdBeanClass;

    /**
     * JavaCmd：方法名
     *
     * @see AnnoTableButton.JavaCmd#methodName()
     */
    @ApiModelProperty(value = "JavaCmd：方法名", example = "c12345")
    private String javaCmdMethodName;

}
