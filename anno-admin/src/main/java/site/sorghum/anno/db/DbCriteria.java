package site.sorghum.anno.db;

import lombok.Data;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.EntityMetadataLoader;

import java.util.List;

/**
 * @author songyinyin
 * @since 2024/1/13 14:23
 */
@Data
public class DbCriteria {

    private String entityName;

    private String tableName;

    /**
     * 查询条件
     */
    private DbCondition condition;

    /**
     * 排序条件
     */
    private DbOrderBy order;

    /**
     * 分页条件
     */
    private DbPage page;

    public static DbCriteria from(AnEntity entity) {
        DbCriteria criteria = new DbCriteria();
        criteria.setEntityName(entity.getEntityName());
        criteria.setTableName(entity.getTableName());
        return criteria;
    }

    public static DbCriteria fromObject(Object entityData) {
        DbCriteria criteria = new DbCriteria();
        criteria.setEntityName(AnnoBeanUtils.getBean(EntityMetadataLoader.class).getEntityName(entityData.getClass()));
        return criteria;
    }

    public static DbCriteria fromClass(Class<?> entityClass) {
        DbCriteria criteria = new DbCriteria();
        criteria.setEntityName(AnnoBeanUtils.getBean(EntityMetadataLoader.class).getEntityName(entityClass));
        return criteria;
    }

    public DbCondition condition() {
        if (condition == null) {
            condition = new DbCondition();
        }
        return condition;
    }

    public List<DbCondition> findCondition(String field) {
        if (condition == null) {
            return null;
        }
        return condition.findCondition(field);
    }

    public List<DbCondition> findConditionByType(QueryType type) {
        if (condition == null) {
            return null;
        }
        return condition.findConditionByType(type);
    }

    public DbOrderBy order() {
        if (order == null) {
            order = new DbOrderBy();
        }
        return order;
    }

    /**
     * 分页
     *
     * @param currentPage 当前页，从 1 开始
     * @param pageSize    每页条数
     */
    public DbPage page(int currentPage, int pageSize) {
        this.page = new DbPage(currentPage, pageSize);
        return this.page;
    }

    /**
     * 只取第一条数据
     */
    public DbPage pageOne() {
        return page(1, 1);
    }

    public DbPage getPageOrDefault() {
        if (page == null) {
            page = new DbPage(1, 10);
        }
        return page;
    }

    // ------------------ 条件 ------------------

    public DbCriteria addCondition(String field, QueryType operator, Object... values) {
        condition().create(field, operator, values);
        return this;
    }

    public DbCriteria eq(String column, Object value) {
        condition().eq(column, value);
        return this;
    }

    public DbCriteria ne(String column, Object value) {
        condition().ne(column, value);
        return this;
    }

    public DbCriteria gt(String column, Object value) {
        condition().gt(column, value);
        return this;
    }

    public DbCriteria ge(String column, Object value) {
        condition().ge(column, value);
        return this;
    }

    public DbCriteria lt(String column, Object value) {
        condition().lt(column, value);
        return this;
    }

    public DbCriteria le(String column, Object value) {
        condition().le(column, value);
        return this;
    }

    public DbCriteria in(String column, Object... values) {
        condition().in(column, values);
        return this;
    }

    public DbCriteria notIn(String column, Object... values) {
        condition().notIn(column, values);
        return this;
    }

    public DbCriteria isNull(String column) {
        condition().isNull(column);
        return this;
    }

    public DbCriteria isNotNull(String column) {
        condition().isNotNull(column);
        return this;
    }

    public DbCriteria exists(String column, Object... values) {
        condition().exists(column, values);
        return this;
    }

    public DbCriteria notExists(String column, Object... values) {
        condition().notExists(column, values);
        return this;
    }

    public DbCriteria between(String column, Object... values) {
        condition().between(column, values);
        return this;
    }

    public DbCriteria notBetween(String column, Object... values) {
        condition().notBetween(column, values);
        return this;
    }
}
