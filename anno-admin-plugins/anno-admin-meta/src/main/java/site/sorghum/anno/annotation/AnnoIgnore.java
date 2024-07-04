package site.sorghum.anno.annotation;


import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.lang.annotation.*;

/**
 * Anno 忽略bytebuddy注入
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoIgnore {

}
