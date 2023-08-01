package site.sorghum.anno.spring.db.spring;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.inject.Inject;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import site.sorghum.anno.spring.db.util.InjectUtil;

import java.lang.reflect.Field;

/**
 * 组件扫描
 *
 * @author Sorghum
 * @since 2022/09/29
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class MustQuickComponentScanner implements InstantiationAwareBeanPostProcessor {
    /**
     * 统计注入耗时
     */
    public static long duration = 0L;
    /**
     * 发布过程Bean实例化后
     * 扫描所有的@Inject修饰的Bean
     *
     * @param bean     bean
     * @param beanName bean名字
     * @return boolean
     * @throws BeansException 豆子异常
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean isFastAsync = InjectUtil.isInjectClazz(bean);
        if (isFastAsync) {
            Class<?> targetClass = InjectUtil.targetClass(bean);
            // 获取所有@Db修饰的字段
            Field[] fields = ReflectUtil.getFields(targetClass, field -> AnnotationUtil.getAnnotation(field, Db.class) != null);
            // 遍历字段
            for (Field field : fields) {
                // 获取@Db注解
                Db db = field.getAnnotation(Db.class);
                // 获取字段类型
                Class<?> beanClazz = field.getType();
                if (beanClazz.getSimpleName().endsWith("Dao")){
                    DbContext dbContext = SpringUtil.getBean(DbContext.class);
                    ReflectUtil.setFieldValue(bean, field, dbContext.mapper(beanClazz));
                }else {
                    Object fastGetBean = SpringUtil.getBean(field.getType());
                    ReflectUtil.setFieldValue(bean, field, fastGetBean);
                }

            }
        }
        stopWatch.stop();
        duration += stopWatch.getTotalTimeMillis();
        return true;
    }

}
