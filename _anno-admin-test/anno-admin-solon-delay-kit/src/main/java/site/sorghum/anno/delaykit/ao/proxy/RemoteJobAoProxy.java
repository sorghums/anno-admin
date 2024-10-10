package site.sorghum.anno.delaykit.ao.proxy;

import jakarta.inject.Inject;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.delaykit.ao.RemoteJobAo;
import site.sorghum.anno.delaykit.server.JobRegister;

import java.util.List;

@Component
public class RemoteJobAoProxy implements AnnoBaseProxy<RemoteJobAo> {
    @Inject
    JobRegister register;

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<RemoteJobAo> page) {
        AnnoBaseProxy.super.afterFetch(criteria, page);
        List<String> jobNames = register.allJobNames();
        List<RemoteJobAo> remoteJobAos = jobNames.stream().map(
            it -> {
                RemoteJobAo remoteJobAo = new RemoteJobAo();
                remoteJobAo.setJobName(it);
                remoteJobAo.setId(it);
                return remoteJobAo;
            }
        ).toList();
        page.setList(remoteJobAos);
        page.setSize(remoteJobAos.size());
        page.setTotal(remoteJobAos.size());
        page.setCurrentPage(1);
    }
}
