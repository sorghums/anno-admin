package site.sorghum.anno.plugin.manager;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno.plugin.ao.AnDictBase;
import site.sorghum.anno.plugin.ao.AnDictBiz;
import site.sorghum.anno.plugin.ao.AnDictSystem;
import site.sorghum.anno.plugin.dao.AnDictBizDao;
import site.sorghum.anno.plugin.dao.AnDictSystemDao;
import site.sorghum.anno.trans.OnlineDictCache;

import java.util.*;

/**
 * 在线词典管理器
 *
 * @author Sorghum
 * @since 2024/11/22
 */
@Named
public class OnlineDictManager {
    @Inject
    AnDictBizDao anDictBizDao;
    @Inject
    AnDictSystemDao anDictSystemDao;
    @Inject
    OnlineDictCache onlineDictCache;

    public void loadDict() {
        List<AnDictBase> anDictBases = this.getAnDictBases();
        Map<String, List<OnlineDictCache.OnlineDict>> dictBaseMap = this.dictBaseBuild(anDictBases);
        for (Map.Entry<String, List<OnlineDictCache.OnlineDict>> entry : dictBaseMap.entrySet()) {
            onlineDictCache.put(entry.getKey(), entry.getValue());
        }
    }
    private List<AnDictBase> getAnDictBases() {
        List<AnDictBiz> anDictBizList = anDictBizDao.list();
        List<AnDictSystem> anDictSystemList = anDictSystemDao.list();
        List<AnDictBase> anDictBases = new ArrayList<>();
        anDictBases.addAll(anDictBizList);
        anDictBases.addAll(anDictSystemList);
        return anDictBases;
    }

    private Map<String,List<OnlineDictCache.OnlineDict>> dictBaseBuild(List<AnDictBase> anDictBases) {
        Map<String, List<OnlineDictCache.OnlineDict>> dictBaseMap = new HashMap<>();
        List<OnlineDictCache.OnlineDict> onlineDictList = AnDictBase.toDict(anDictBases);
        List<OnlineDictCache.OnlineDict> parentDictList = onlineDictList.stream().filter(it -> StrUtil.isBlank(it.getPid())).toList();
        for (OnlineDictCache.OnlineDict onlineDict : parentDictList) {
            List<OnlineDictCache.OnlineDict> dictBases = onlineDictList.stream().filter(
                it -> Objects.equals(it.getPid(), onlineDict.getId())
            ).toList();
            dictBaseMap.put(onlineDict.getValue(), dictBases);
        }
        return dictBaseMap;
    }

}
