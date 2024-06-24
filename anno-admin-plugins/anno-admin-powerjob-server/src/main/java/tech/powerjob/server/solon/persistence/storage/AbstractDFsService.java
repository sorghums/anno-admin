package tech.powerjob.server.solon.persistence.storage;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.AppContext;
import tech.powerjob.server.solon.extension.dfs.DFsService;

/**
 * AbstractDFsService
 *
 * @author tjq
 * @since 2023/7/28
 */
@Slf4j
public abstract class AbstractDFsService implements DFsService {

    public AbstractDFsService() {
        log.info("[DFsService] invoke [{}]'s constructor", this.getClass().getName());
    }

    abstract public void init(AppContext context);

    protected static final String PROPERTY_KEY = "oms.storage.dfs";

    protected static String fetchProperty(AppContext context, String dfsType, String key) {
        String pKey = String.format("%s.%s.%s", PROPERTY_KEY, dfsType, key);
        return context.cfg().getProperty(pKey);
    }

}
