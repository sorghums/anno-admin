package site.sorghum.anno.solon;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.core.*;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.ProxyBinder;
import org.noear.solon.data.tran.TranExecutor;
import org.noear.solon.data.tran.TranExecutorDefault;
import org.noear.solon.web.staticfiles.StaticMappings;
import org.noear.solon.web.staticfiles.repository.ClassPathStaticRepository;
import org.noear.wood.WoodConfig;
import site.sorghum.anno._annotations.AnnoSerialization;
import site.sorghum.anno._annotations.Primary;
import site.sorghum.anno._annotations.Proxy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.anno.method.MethodTemplateManager;
import site.sorghum.anno.solon.init.InitDdlAndDataService;
import site.sorghum.anno.solon.init.MethodTemplateInitService;
import site.sorghum.anno.solon.interceptor.AnnoSerializationInterceptor;
import site.sorghum.anno.solon.interceptor.TransactionalInterceptor;
import site.sorghum.anno.solon.interceptor.WoodSqlLogInterceptor;
import site.sorghum.anno.utils.MTUtils;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Solon Anno-Admin 插件
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Slf4j
public class XPluginImp implements Plugin {
    private static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";
    private static final String PLUGIN_BASE_PACKAGE = "site.sorghum.plugin";

    @Override
    public void start(AppContext context) throws Throwable {

        AnnoBeanUtils.setBean(new SolonBeanImpl(context));

        i18nSupport();

        // 注册序列化拦截器
        context.beanInterceptorAdd(AnnoSerialization.class, new AnnoSerializationInterceptor());

        context.beanInjectorAdd(Inject.class, new InjectBeanInjector(context));

        context.beanBuilderAdd(Named.class, new NamedBeanBuilder(context));

        tranSupport(context);

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

        // 初始化自带的
        MethodTemplateManager.parse(ANNO_BASE_PACKAGE);
        // 其余后续初始化
        MethodTemplateInitService.packages = packages.stream().filter(
            it -> !it.equals(ANNO_BASE_PACKAGE)
        ).collect(Collectors.toSet());


        // Aviator脚本
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        instance.useLRUExpressionCache(1000);
        instance.addStaticFunctions("mt", MTUtils.class);
        instance.addInstanceFunctions("s", String.class);

        // 加载 anno 元数据
        loadMetadata(context, packages);

        // 前端静态文件
        StaticMappings.add(AnnoConstants.BASE_URL + "/", new ClassPathStaticRepository("/WEB-INF/anno-admin-ui/"));


        // 扫描翻译插件
        context.beanScan(PLUGIN_BASE_PACKAGE);

        // wood 设置
        WoodConfig.isSelectItemEmptyAsNull = true;
        WoodConfig.isUsingValueNull = true;
        WoodConfig.onExecuteAft(new WoodSqlLogInterceptor());

        // 优先 初始化数据库表结构和预置数据，其他模块在创建 bean 时，可能会查库
        context.getBeanAsync(InitDdlAndDataService.class, initDdlAndDataService -> {
            context.getBeanAsync(DataSource.class, dataSource -> {
                try {
                    initDdlAndDataService.initDdl();
                } catch (Throwable e) {
                    log.warn("初始化数据库表结构和预置数据错误:", e);
                }
            });
        });

     }

    /**
     * 缓存和事务支持
     */
    private static void tranSupport(AppContext context) {
        // 添加事务控制支持，see: org.noear.solon.data.integration.XPluginImp
        if (Solon.app().enableTransaction()) {
            context.wrapAndPut(TranExecutor.class, TranExecutorDefault.global);
            context.beanInterceptorAdd(Transactional.class, new TransactionalInterceptor(), 121);
        }
    }

    /**
     * 加载 anno 元数据
     */
    private void loadMetadata(AppContext context, Set<String> packages) {
        MetadataManager metadataManager = context.getBean(MetadataManager.class);
        metadataManager.setScanPackages(packages);
        HashSet<Class<?>> classSet = new HashSet<>();
        // 所有类
        for (String scanPackage : packages) {
            classSet.addAll(ClassUtil.scanPackage(scanPackage));
        }
        for (Class<?> clazz : classSet) {
            if (clazz.isInterface()) {
                continue;
            }
            // annoMain 主注释
            AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
            if (annoMain != null) {
                // 加载anEntity
                metadataManager.loadEntity(clazz);
            }
        }
        metadataManager.refresh();
    }

    static class InjectBeanInjector implements BeanInjector<Inject> {

        public InjectBeanInjector(AppContext context) {
            this.context = context;
        }

        AppContext context;

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

        public NamedBeanBuilder(AppContext context) {
            this.context = context;
        }

        AppContext context;

        @Override
        public void doBuild(Class<?> clz, BeanWrap bw, Named anno) throws Throwable {
            if (StrUtil.isNotBlank(anno.value())) {
                ReflectUtil.setFieldValue(bw, "name", anno.value());
            } else {
                // 默认给个名字
                ReflectUtil.setFieldValue(bw, "name", StrUtil.lowerFirst(clz.getSimpleName()));
            }

            // 是否是typed
            Primary primary = clz.getAnnotation(Primary.class);
            boolean typed = Optional.ofNullable(primary).map(Primary::value).orElse(true);
            ReflectUtil.setFieldValue(bw, "typed", typed);

            // see: AppContext#beanComponentized
            //尝试提取函数并确定自动代理
            context.beanExtractOrProxy(bw);
            //添加bean形态处理
            context.beanShapeRegister(bw.clz(), bw, bw.clz());
            //注册到容器
            context.beanRegister(bw, bw.name(), bw.typed());
            //单例，进行事件通知
            if (bw.singleton()) {
                EventBus.publish(bw.raw()); //@deprecated
                context.wrapPublish(bw);
            }

            // 生成代理类
            Proxy annotation = clz.getAnnotation(Proxy.class);
            if (annotation != null) {
                ProxyBinder.global().binding(bw);
            }

        }
    }

    public void i18nSupport() {
        I18nUtil.setI18nService(org.noear.solon.i18n.I18nUtil::getMessage);
    }

}
