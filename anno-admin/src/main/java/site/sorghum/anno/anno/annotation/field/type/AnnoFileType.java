package site.sorghum.anno.anno.annotation.field.type;

import java.lang.annotation.*;

/**
 * Anno 图像类型
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoFileType {
    /**
     * 文件类型
     *
     * @return {@link String}
     */
    String fileType() default "*";

    /**
     * 文件最大计数
     *
     * @return int
     */
    int fileMaxCount() default 1;

    /**
     * 文件最大大小 单位Mb
     *
     * @return int
     */
    int fileMaxSize() default 5;
}
