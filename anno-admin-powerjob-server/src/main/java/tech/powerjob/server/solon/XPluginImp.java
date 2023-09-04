package tech.powerjob.server.solon;

import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.config.ConfigLoader;
import org.simplejavamail.mailer.MailerBuilder;
import tech.powerjob.server.solon.common.async.PjAsync;
import tech.powerjob.server.solon.common.async.PjAsyncInterceptor;
import tech.powerjob.server.solon.core.lock.UseCacheLock;
import tech.powerjob.server.solon.core.lock.UseCacheLockAspect;
import tech.powerjob.server.solon.remote.server.redirector.DesignateServer;
import tech.powerjob.server.solon.remote.server.redirector.DesignateServerAspect;

import java.util.Properties;

/**
 * @author songyinyin
 * @since 2023/8/26 17:44
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) throws Throwable {
        context.beanInterceptorAdd(PjAsync.class, new PjAsyncInterceptor(context));
        context.beanInterceptorAdd(DesignateServer.class, new DesignateServerAspect(context));
        context.beanInterceptorAdd(UseCacheLock.class, new UseCacheLockAspect(context));

        initMail(context);

        context.beanScan(XPluginImp.class.getPackageName());
    }

    private void initMail(AopContext context) {
        String prefix = "simplejavamail";
        Properties props = context.cfg().getProp(prefix);
        if (props.isEmpty()) {
            return;
        }
        Properties smprops = new Properties();
        props.forEach((k,v)->{
            smprops.put(prefix+"."+k, v);
        });
        ConfigLoader.loadProperties(smprops, true);
        Mailer mailer = MailerBuilder.buildMailer();
        context.wrapAndPut(Mailer.class, mailer);
    }
}
