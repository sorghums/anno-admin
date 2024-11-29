package site.sorghum.anno.proxy;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.IPage;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.ao.CopyHisTaskAo;
import site.sorghum.anno.ao.DoneHisTaskAo;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.service.AnnoFlowService;

@Component
public class CopyHistoryAoProxy implements AnnoBaseProxy<CopyHisTaskAo> {

    @Inject
    AnnoFlowService annoFlowService;

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<CopyHisTaskAo> page) {
        DbCondition condition = criteria.condition();
        String flowName = getValueStringFromJavaCriteria(condition, CopyHisTaskAo.class,"flowName");
        DoneHisTaskAo doneHisTaskAo = new DoneHisTaskAo();
        doneHisTaskAo.setFlowName(flowName);
        DbPage dbPageQuery = criteria.getPage();
        IPage<CopyHisTaskAo> doPage = annoFlowService.copyPage(
            doneHisTaskAo,
            dbPageQuery.getPage(),
            dbPageQuery.getPageSize()
        );
        page.setPage(true);
        page.setList(doPage.getList());
        page.setTotal(doPage.getTotal());
        page.setSize(doPage.getSize());
    }
}
