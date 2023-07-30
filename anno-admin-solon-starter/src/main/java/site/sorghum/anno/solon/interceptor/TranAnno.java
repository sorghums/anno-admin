package site.sorghum.anno.solon.interceptor;

import jakarta.transaction.Transactional;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.data.tran.TranIsolation;
import org.noear.solon.data.tran.TranPolicy;

import java.lang.annotation.Annotation;

/**
 * @author songyinyin
 * @since 2023/7/30 21:04
 */
public class TranAnno implements Tran {

    private final Transactional transactional;

    public TranAnno(Transactional transactional) {
        this.transactional = transactional;
    }

    @Override
    public TranPolicy policy() {
        switch (transactional.value()) {
            case REQUIRES_NEW:
                return TranPolicy.requires_new;
            case SUPPORTS:
                return TranPolicy.supports;
            case NOT_SUPPORTED:
                return TranPolicy.not_supported;
            case MANDATORY:
                return TranPolicy.mandatory;
            case NEVER:
                return TranPolicy.never;
            case REQUIRED:
            default:
                return TranPolicy.required;
        }
    }

    @Override
    public TranIsolation isolation() {
        return TranIsolation.unspecified;
    }

    @Override
    public boolean readOnly() {
        return false;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Tran.class;
    }
}
