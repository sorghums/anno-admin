package site.sorghum.anno.plugin.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.plugin.ao.AnRole;
import site.sorghum.anno.plugin.ao.AnSql;

/**
 * @author Sorghum
 */
@Named
public class AnSqlDao implements AnnoBaseDao<AnSql> {
    @Inject
    DbService dbService;

    @Inject
    MetadataManager metadataManager;
    /**
     * 按版本查询
     *
     * @param version 版本
     * @return {@link AnSql}
     */
    public AnSql queryByVersion(String version) {
        return sqlOne(
            "select * from an_sql where version = ?",
            version);
    }

    @Override
    public DbService dbService() {
        return dbService;
    }

    ;

    @Override
    public MetadataManager metadataManager() {
        return metadataManager;
    }

    @Override
    public Class<AnSql> entityClass() {
        return AnSql.class;
    }
}
