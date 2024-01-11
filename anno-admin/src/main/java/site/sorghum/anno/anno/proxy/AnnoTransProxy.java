package site.sorghum.anno.anno.proxy;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import org.springframework.beans.factory.annotation.Autowired;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.trans.AnnoTransService;

import java.util.List;

/**
 * 翻译代理
 *
 * @author Sorghum
 * @since 2023/11/22
 */
@Component
@org.springframework.stereotype.Component
public class AnnoTransProxy implements AnnoBaseProxy<Object> {

    @Inject
    @Autowired
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
    public void afterFetch(Class<Object> objectClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<Object> page) {
        AnnoBaseProxy.super.afterFetch(objectClass, dbConditions, pageParam, page);
        // 获取数组的第一个元素的类型
        if (page.getList() ==null || page.getList().isEmpty()){
            return;
        }
        Object item = page.getList().get(0);
        transService.trans(page.getList(),item.getClass());
    }
}
