package site.sorghum.anno.plugin.dao;

import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.plugin.ao.AnSql;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

@Namespace("site.sorghum.anno.modular.system.dao")
public interface AnSqlDao extends AnnoBaseMapper<AnSql> {

    @Sql("select * from an_sql where version = ?")
    AnSql queryByVersion(String version);
}
