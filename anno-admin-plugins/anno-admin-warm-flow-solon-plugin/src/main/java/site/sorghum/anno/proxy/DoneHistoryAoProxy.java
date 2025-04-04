package site.sorghum.anno.proxy;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.IPage;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.ao.DoneHisTaskAo;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.service.AnnoFlowService;

@Component
public class DoneHistoryAoProxy implements AnnoBaseProxy<DoneHisTaskAo> {

    @Inject
    AnnoFlowService annoFlowService;

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<DoneHisTaskAo> page) {
        DbPage dbPageQuery = criteria.getPage();
        IPage<DoneHisTaskAo> doPage = annoFlowService.donePage(
            new DoneHisTaskAo(),
            dbPageQuery.getPage(),
            dbPageQuery.getPageSize()
        );
        page.setPage(true);
        page.setList(doPage.getList());
        page.setTotal(doPage.getTotal());
        page.setSize(doPage.getSize());
    }
}
