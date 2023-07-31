package site.sorghum.anno.solon;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.noear.solon.Solon;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.solon.core.*;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.data.annotation.CachePut;
import org.noear.solon.data.annotation.CacheRemove;
import org.noear.solon.data.around.CacheInterceptor;
import org.noear.solon.data.around.CachePutInterceptor;
import org.noear.solon.data.around.CacheRemoveInterceptor;
import org.noear.solon.data.cache.CacheLib;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.data.cache.CacheServiceWrapConsumer;
import org.noear.solon.data.cache.LocalCacheService;
import org.noear.solon.data.tran.TranExecutor;
import org.noear.solon.data.tran.TranExecutorImp;
import org.noear.solon.proxy.ProxyUtil;
import org.noear.solon.proxy.integration.UnsupportedUtil;
import site.sorghum.anno.anno.Primary;
import site.sorghum.anno.anno.Proxy;
import site.sorghum.anno.common.AnnoBeanUtils;
import site.sorghum.anno.i18n.I18nService;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.global.AnnoScan;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoFieldCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.solon.interceptor.TransactionalInterceptor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Solon Anno-Admin 插件
 *
 * @author sorghum
 * @since 2023/05/20
 */
@ProxyComponent
public class XPluginImp implements Plugin {
    private static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";

    @Override
    public void start(AopContext context) {

        i18nSupport();

        context.beanInjectorAdd(Inject.class, new InjectBeanInjector(context));

        context.beanBuilderAdd(Named.class, new NamedBeanBuilder(context));

        cacheAndTran(context);

        // 扫描包
        context.beanScan(ANNO_BASE_PACKAGE);
        Class<?> source = Solon.app().source();
        Set<String> packages = CollUtil.newHashSet(source.getPackage().getName(), ANNO_BASE_PACKAGE);
        AnnoScan annotation = AnnotationUtil.getAnnotation(source, AnnoScan.class);
        if (Objects.nonNull(annotation)) {
            String[] value = annotation.scanPackage();
            if (value.length > 0) {
                packages.addAll(CollUtil.newArrayList(value));
            }
        }

        // 加载 anno 元数据
        loadMetadata(context, packages);

        AnnoBeanUtils.setBean(new SolonBeanImpl(context));

    }

    /**
     * 缓存和事务支持
     */
    private static void cacheAndTran(AopContext context) {
        // 添加事务控制支持，see: org.noear.solon.data.integration.XPluginImp
        if (Solon.app().enableTransaction()) {
            context.wrapAndPut(TranExecutor.class, TranExecutorImp.global);

            context.beanInterceptorAdd(Transactional.class, new TransactionalInterceptor(), 121);
        }

        // 添加缓存控制支持，see: org.noear.solon.data.integration.XPluginImp
        if (Solon.app().enableCaching()) {
            CacheLib.cacheServiceAddIfAbsent("", LocalCacheService.instance);

            context.subWrapsOfType(CacheService.class, new CacheServiceWrapConsumer());

            context.lifecycle(-99, () -> {
                if (!context.hasWrap(CacheService.class)) {
                    context.wrapAndPut(CacheService.class, LocalCacheService.instance);
                }
            });

            context.beanInterceptorAdd(CachePut.class, new CachePutInterceptor(), 110);
            context.beanInterceptorAdd(CacheRemove.class, new CacheRemoveInterceptor(), 110);
            context.beanInterceptorAdd(Cache.class, new CacheInterceptor(), 111);
        }
    }

    /**
     * 加载 anno 元数据
     */
    private void loadMetadata(AopContext context, Set<String> packages) {
        MetadataManager metadataManager = context.getBean(MetadataManager.class);

        for (String scanPackage : packages) {
            Set<Class<?>> classes = ClassUtil.scanPackage(scanPackage);
            for (Class<?> clazz : classes) {
                if (clazz.isInterface()) {
                    continue;
                }
                AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
                if (annoMain != null) {

                    metadataManager.loadEntity(clazz);
                    // 缓存处理类
                    AnnoClazzCache.put(clazz.getSimpleName(), clazz);
                }
                // 缓存字段信息
                AnnoUtil.getAnnoFields(clazz).forEach(
                    field -> {
                        String columnName = AnnoUtil.getColumnName(field);
                        AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field);
                    }
                );
            }
        }
    }

    static class InjectBeanInjector implements BeanInjector<Inject> {

        public InjectBeanInjector(AopContext context) {
            this.context = context;
        }

        AopContext context;

        @Override
        public void doInject(VarHolder varH, Inject anno) {
            Annotation[] annotations = varH.getAnnoS();
            String beanName = Optional.ofNullable(annotations).stream().flatMap(Arrays::stream)
                .filter(annotation -> annotation.annotationType().equals(Named.class))
                .findFirst()
                .map(annotation -> ((Named) annotation).value()).orElse("");
            context.beanInject(varH, beanName);
        }
    }

    static class NamedBeanBuilder implements BeanBuilder<Named> {

        public NamedBeanBuilder(AopContext context) {
            this.context = context;
        }

        AopContext context;

        @Override
        public void doBuild(Class<?> clz, BeanWrap bw, Named anno) throws Throwable {
            if (StrUtil.isNotBlank(anno.value())) {
                ReflectUtil.setFieldValue(bw, "name", anno.value());
            }
            // 添加bean形态处理
            context.beanShapeRegister(clz, bw, clz);
            // 是否是typed
            Primary primary = clz.getAnnotation(Primary.class);
            boolean typed = Optional.ofNullable(primary).map(Primary::value).orElse(true);
            // 注册到容器
            context.beanRegister(bw, anno.value(), typed);
            // 尝试提取函数
            context.beanExtract(bw);

            // 生成代理类
            Proxy annotation = clz.getAnnotation(Proxy.class);
            if (annotation != null) {
                ProxyUtil.binding(bw, anno.value(), true);

                if (Solon.cfg().isDebugMode()) {
                    UnsupportedUtil.check(clz, bw.context(), anno);
                }
            }

        }
    }

    public void i18nSupport(){
        I18nUtil.setI18nService(new I18nService() {
            @Override
            public String getMessage(String key) {
                return org.noear.solon.i18n.I18nUtil.getMessage(key);
            }
        });
    }

}
