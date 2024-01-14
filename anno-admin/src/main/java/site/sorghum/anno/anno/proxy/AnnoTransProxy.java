package site.sorghum.anno.anno.proxy;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.trans.AnnoTransService;

import java.util.List;

/**
 * 翻译代理
 *
 * @author Sorghum
 * @since 2023/11/22
 */
@Named
public class AnnoTransProxy implements AnnoBaseProxy<Object> {

    @Inject
    AnnoTransService transService;

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(Object.class)
        };
    }

    @Override
    public int index() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void afterFetch(DbCriteria criteria, AnnoPage<Object> page) {
        // 获取数组的第一个元素的类型
        if (page.getList() ==null || page.getList().isEmpty()){
            return;
        }
        Object item = page.getList().get(0);
        transService.trans(page.getList(),item.getClass());
    }
}
