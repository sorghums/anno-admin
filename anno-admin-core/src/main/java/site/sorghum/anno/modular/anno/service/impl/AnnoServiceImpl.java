package site.sorghum.anno.modular.anno.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.wood.IPage;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.proxy.AnnoPreBaseProxy;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;
import site.sorghum.anno.modular.anno.service.AnnoService;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Anno服务
 *
 * @author sorghum
 * @since 2023/05/20
 */
@SuppressWarnings("unchecked")
@ProxyComponent
@Slf4j
public class AnnoServiceImpl implements AnnoService {

    @Inject
    PermissionProxy permissionProxy;

    @Db
    DbContext dbContext;

    @Override
    public <T> String sql(Class<T> clazz,String sql) {
        try {
            // 如果有配置逻辑删除
            AnnoRemove annoRemove = AnnoUtil.getAnnoRemove(clazz);
            if (annoRemove.removeType() == 1) {
                sql = sql + " and " + annoRemove.removeField() + " = " + annoRemove.notRemoveValue();
            }
            return sql;
        } catch (Exception e) {
            log.error("AnnoService.page error:{}", e.getMessage());
            throw new BizException("权限不足或系统异常",e);
        }
    }

    @Override
    public <T> String m2mSql(Map<?,? > param) {
        if (StrUtil.isBlank(MapUtil.getStr(param,"mediumTableClass"))){
            return "";
        }
        String mediumOtherField = MapUtil.getStr(param,"mediumOtherField");
        String otherValue =MapUtil.getStr(param,"joinValue");
        String mediumThisField = MapUtil.getStr(param,"mediumThisField");

        Class<?> mediumCLass = AnnoClazzCache.get(MapUtil.getStr(param,"mediumTableClass"));
        String mediumTable = AnnoUtil.getTableName(mediumCLass);
        String sql = "select "+mediumThisField+" from " + mediumTable + " where " + mediumOtherField + " = '" + otherValue + "'";
        return sql(mediumCLass,sql);
    }

    @Override
    public <T> List<AnnoTreeDTO<String>> annoTrees(Class<T> tClass,List<T> dataList) {
        try {
            AnnoMain annoMain = AnnoUtil.getAnnoMain(tClass);
            AnnoTree annoTree = annoMain.annoTree();
            return AnnoUtil.buildAnnoTree(dataList, annoTree.label(), annoTree.key(), annoTree.parentKey());
        } catch (Exception e) {
            log.error("AnnoService.annoTrees error:{}", e.getMessage());
            throw new BizException("权限不足或系统异常", e);
        }
    }
}
