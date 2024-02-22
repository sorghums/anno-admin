package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.util.MD5Util;
import site.sorghum.anno._ddl.InitDataService;
import site.sorghum.anno.method.resource.JarResource;
import site.sorghum.anno.plugin.ao.AnSql;
import site.sorghum.anno.plugin.dao.AnSqlDao;
import site.sorghum.anno.plugin.service.AnSqlService;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Named
@Slf4j
public class AnSqlServiceImpl implements AnSqlService {

    @Inject
    AnnoProperty annoProperty;
    @Inject
    AnSqlDao anSqlDao;

    @Inject
    InitDataService initDataService;

    public static final int STATE_NOT_RUN = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL = 2;

    @Override
    public void runSql(Map<String, Object> data) {
        AnSql anSql = anSqlDao.findById((Serializable) data.get("id"));

        String sqlContent = (String) data.get("sqlContent");
        try {
            initDataService.init(sqlContent);
            anSql.setState(1);
        } catch (Exception e) {
            anSql.setState(2);
            anSql.setErrorLog(
                ExceptionUtil.stacktraceToString(e)
            );
            throw new BizException("手动执行 失败 => " + e.getMessage());
        } finally {
            anSql.setRunTime(DateUtil.date());
            anSqlDao.updateById(anSql);
        }
    }

    /**
     * 执行资源文件中的sql，一般是 Jar 包中的 sql 文件
     *
     * <p>资源文件的内容有变化时，会重新执行该 SQL 文件</p>
     *
     * @param resource 资源文件
     */
    @Override
    public void runResourceSql(Resource resource) throws IOException {
        if (resource == null) {
            return;
        }
        String version = getVersion(resource);

        AnSql anSql = anSqlDao.queryByVersion(version);
        String sqlContent = IoUtil.read(resource.getUrl().openStream(), StandardCharsets.UTF_8);
        String sqlMd5 = MD5Util.digestHex(sqlContent);

        // md5 相等时，不再执行
        if (anSql != null && StrUtil.equals(anSql.getSqlMd5(), sqlMd5) && anSql.getState() == STATE_SUCCESS) {
            return;
        }

        // SQL 初次执行或者内容有变化时，重新执行
        AnSql newSql = new AnSql();
        newSql.setVersion(version);
        newSql.setSqlContent(sqlContent);
        newSql.setSqlMd5(sqlMd5);

        if (anSql == null) {
            runSql(newSql, resource);
        } else {
            newSql.setId(anSql.getId());
            runSql(newSql, resource);
        }
    }

    private String getVersion(Resource resource) {
        String version = resource.getName();
        if (resource instanceof FileResource fileResource) {
            String path = fileResource.getFile().getPath();
            int indexOf = path.lastIndexOf("classes");
            if (indexOf > 0) {
                version = path.substring(indexOf + 8);
            } else {
                version = path;
            }
        }
        if (resource instanceof JarResource jarResource) {
            version = jarResource.getJarFile().getName() + "!" + version;
        }
        return version;
    }

    private void runSql(AnSql sql, Resource resource) {
        if (annoProperty.getIsAutoMaintainInitData()) {
            try {
                sql.setRunTime(DateUtil.date());

                initDataService.init(sql.getSqlContent());
                sql.setState(STATE_SUCCESS);
            } catch (Exception e) {
                sql.setState(STATE_FAIL);
                sql.setErrorLog(ExceptionUtil.stacktraceToString(e));
                log.error("parse or execute sql error, resource: {}", resource);
            }
        } else {
            sql.setState(STATE_NOT_RUN);
        }
        anSqlDao.saveOrUpdate(sql);
    }
}
