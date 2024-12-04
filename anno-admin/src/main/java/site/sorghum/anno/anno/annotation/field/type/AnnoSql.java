package site.sorghum.anno.anno.annotation.field.type;

public @interface AnnoSql {
    /**
     * SQL语句, 优先级高于value
     * 必须返回两列，列名分别为 id,label,pid[仅树Tree需要返回]
     * 比如 select id,label,pid from table where del_flag = 0 order by id desc
     *
     * @return {@link String}
     */
    String sql();

    /**
     * 数据库名称
     *
     * @return {@link String}
     */
    String dbName() default "";
}