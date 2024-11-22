package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.plugin.ao.AnDictBase;
import site.sorghum.anno.plugin.manager.OnlineDictManager;

@Named
@Slf4j
public class AnDictBaseProxy implements AnnoBaseProxy<AnDictBase> {

    @Inject
    private OnlineDictManager onlineDictManager;


    @Override
    public void beforeAdd(AnDictBase data) {
        if (StrUtil.isBlank(data.getValue())) {
            throw new BizException("字典值不能为空！");
        }
        log.info("beforeAdd: {}", data);
    }

    @Override
    public void afterAdd(AnDictBase data) {
        onlineDictManager.loadDict();
    }

    @Override
    public void afterUpdate(AnDictBase data) {
        onlineDictManager.loadDict();
    }

    @Override
    public void afterDelete(DbCriteria criteria) {
        onlineDictManager.loadDict();
    }
}
