package site.sorghum.anno.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 查询参数
 *
 * @author sorghum
 * @since 2023/07/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbCondition {

    /**
     * 字段名
     */
    String field;

    /**
     * 查询类型
     */
    @Builder.Default
    QueryType type = QueryType.AND;

    /**
     * 值
     */
    List<Object> values = new ArrayList<>();


    /**
     * 不执行
     */
    boolean noExecute = false;

    public List<DbCondition> findCondition(String field) {
        return findCondition0(field, pair -> StrUtil.equals(pair.getKey().getField(), (String) pair.getValue()));
    }

    public List<DbCondition> findConditionByType(QueryType type) {
        return findCondition0(type, pair -> Objects.equals(pair.getKey().getType(), pair.getValue()));
    }

    public List<DbCondition> findCondition0(Object object, Predicate<Pair<DbCondition, Object>> predicate) {
        List<DbCondition> result = new ArrayList<>();
        if (predicate.test(Pair.of(this, object))) {
            result.add(this);
        }
        for (Object value : values) {
            if (value instanceof DbCondition) {
                List<DbCondition> conditions = ((DbCondition) value).findCondition0(object, predicate);
                if (CollUtil.isNotEmpty(conditions)) {
                    result.addAll(conditions);
                }
            }
        }
        return result;
    }

    public DbCondition like(String field, String value) {
        return create(field, QueryType.LIKE, "%" + value + "%");
    }

    public DbCondition begin(String field, String value) {
        return create(field, QueryType.LIKE, value + "%");
    }

    public DbCondition end(String field, String value) {
        return create(field, QueryType.LIKE, "%" + value);
    }

    public DbCondition in(String field, Object... values) {
        return this.create(field, QueryType.IN, values);
    }

    public DbCondition notIn(String field, Object... values) {
        return this.create(field, QueryType.NOT_IN, values);
    }

    public DbCondition eq(String field, Object value) {
        return create(field, QueryType.EQ, value);
    }

    public DbCondition ne(String field, Object... values) {
        return this.create(field, QueryType.NE, values);
    }

    public DbCondition gt(String field, Object... values) {
        return this.create(field, QueryType.GT, values);
    }

    public DbCondition ge(String field, Object... values) {
        return this.create(field, QueryType.GE, values);
    }

    public DbCondition lt(String field, Object... values) {
        return this.create(field, QueryType.LT, values);
    }

    public DbCondition le(String field, Object... values) {
        return this.create(field, QueryType.LE, values);
    }

    public DbCondition isNull(String field) {
        return this.create(field, QueryType.IS_NULL);
    }

    public DbCondition isNotNull(String field) {
        return this.create(field, QueryType.IS_NOT_NULL);
    }

    public DbCondition exists(String field, Object... values) {
        return this.create(field, QueryType.EXISTS, values);
    }

    public DbCondition notExists(String field, Object... values) {
        return this.create(field, QueryType.NOT_EXISTS, values);
    }

    public DbCondition between(String field, Object... values) {
        return this.create(field, QueryType.BETWEEN, values);
    }

    public DbCondition notBetween(String field, Object... values) {
        return this.create(field, QueryType.NOT_BETWEEN, values);
    }

    public DbCondition create(String field, QueryType operator, Object... values) {
        DbCondition condition = new DbCondition(field, operator, Arrays.asList(values),false);
        this.values.add(condition);
        return this;
    }

}
