package tech.powerjob.server.solon.remote.aware;

import tech.powerjob.server.solon.common.aware.PowerJobAware;
import tech.powerjob.server.solon.remote.transporter.TransportService;

/**
 * TransportServiceAware
 *
 * @author tjq
 * @since 2023/3/4
 */
public interface TransportServiceAware extends PowerJobAware {

    void setTransportService(TransportService transportService);
}
