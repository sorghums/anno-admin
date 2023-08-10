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
        return switch (transactional.value()) {
            case REQUIRES_NEW -> TranPolicy.requires_new;
            case SUPPORTS -> TranPolicy.supports;
            case NOT_SUPPORTED -> TranPolicy.not_supported;
            case MANDATORY -> TranPolicy.mandatory;
            case NEVER -> TranPolicy.never;
            default -> TranPolicy.required;
        };
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
