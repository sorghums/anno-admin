package site.sorghum.anno.solon.interceptor;

import jakarta.transaction.Transactional;
import org.noear.solon.core.ValHolder;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import org.noear.solon.data.tran.TranUtils;

/**
 * @author songyinyin
 * @since 2023/7/30 21:02
 */
public class TransactionalInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        ValHolder val0 = new ValHolder();

        Transactional annotation = inv.method().getAnnotation(Transactional.class);
        if (annotation == null) {
            return inv.invoke();
        }
        TranUtils.execute(new TranAnno(annotation), () -> val0.value = inv.invoke());

        return val0.value;
    }
}
