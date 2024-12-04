package site.sorghum.anno.anno.annotation.field.type;

import lombok.Data;

import java.lang.annotation.Annotation;

@Data
public class AnnoSqlImpl implements AnnoSql {
    /**
     * sql语句
     */
    String sql = "";

    /**
     * 数据库名称
     */
    String dbName = "";

    @Override
    public String sql() {
        return this.sql;
    }

    @Override
    public String dbName() {
        return this.dbName;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoSql.class;
    }
}
