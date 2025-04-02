package site.sorghum.anno.plugin.dao;

import jakarta.inject.Named;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.plugin.ao.AnSql;

/**
 * @author Sorghum
 */
@Named
public class AnSqlDao implements AnnoBaseDao<AnSql> {

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
}
