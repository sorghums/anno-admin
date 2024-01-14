package site.sorghum.anno.suppose.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.SneakyThrows;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.service.DbService;

@Getter
@Named
public class VirtualJoinTableProxy<T> implements AnnoBaseProxy<T> {

    @Inject
    protected DbService dbService;

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<T> page) {
        AnnoPage<T> virtualIPage = dbService.page(criteria);
        page.setTotal(virtualIPage.getTotal());
        page.setList(virtualIPage.getList());
        page.setList(virtualIPage.getList());
    }

}
