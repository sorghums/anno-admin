package site.sorghum.anno.db.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.wood.DbTableQuery;

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
     * 查询类型 0 Eq 1 Like 2 In 3 not in 4 neq 5 gt 6 lt 7 gte 8 lte 9 自定义SQl
     */
    @Builder.Default
    QueryType type = QueryType.EQ;

    /**
     * 0 and 1 or
     */
    @Builder.Default
    AndOr andOr = AndOr.AND;
    /**
     * 字段名
     */
    String field;

    /**
     * 值
     */
    Object value;

    /**
     * 填充数据
     */
    public void woodFill(DbTableQuery dbTableQuery) {
        // 去除别名带来的影响
        if (field.contains("as")){
            field = field.split("as")[0];
        }
        switch (type) {
            case ORDER_BY -> {
                if ("asc".equals(value)){
                    dbTableQuery.orderByAsc(field);
                }else {
                    dbTableQuery.orderByDesc(field);
                }
            }
            case EQ -> {
                if (andOr == AndOr.AND) {
                    dbTableQuery.andEq(field, value);
                } else {
                    dbTableQuery.orEq(field, value);
                }

            }
            case LIKE -> {
                String valueString = value.toString();
                boolean hasBlurValue = valueString.contains("%") || valueString.contains("_") || valueString.contains("?");
                valueString = hasBlurValue ? valueString : "%" + valueString + "%";
                if (andOr == AndOr.AND) {
                    {
                        if (value instanceof String || value instanceof StringBuilder || value instanceof StringBuffer) {
                            dbTableQuery.andLk(field, valueString);
                        }
                    }
                } else {
                    {
                        if (value instanceof String || value instanceof StringBuilder || value instanceof StringBuffer) {
                            dbTableQuery.orLk(field, valueString);
                        }
                    }
                }
            }
            case IN -> {
                if (andOr == AndOr.AND) {
                    if (value instanceof Iterable<?> iterable) {
                        dbTableQuery.andIn(field, iterable);
                    }
                } else {
                    if (value instanceof Iterable<?> iterable) {
                        dbTableQuery.orIn(field, iterable);
                    }
                }
            }
            case NOT_IN -> {
                if (andOr == AndOr.AND) {
                    if (value instanceof Iterable<?> iterable) {
                        dbTableQuery.andNin(field, iterable);
                    }
                } else {
                    if (value instanceof Iterable<?> iterable) {
                        dbTableQuery.orNin(field, iterable);
                    }
                }
            }
            case NEQ -> {
                if (andOr == AndOr.AND) {
                    dbTableQuery.andNeq(field, value);
                } else {
                    dbTableQuery.orNeq(field, value);
                }
            }
            case GT -> {
                if (andOr == AndOr.AND) {
                    dbTableQuery.andGt(field, value);
                } else {
                    dbTableQuery.orGt(field, value);
                }
            }
            case LT -> {
                if (andOr == AndOr.AND) {
                    dbTableQuery.andLt(field, value);
                } else {
                    dbTableQuery.orLt(field, value);
                }
            }
            case GTE -> {
                if (andOr == AndOr.AND) {
                    dbTableQuery.andGte(field, value);
                } else {
                    dbTableQuery.orGte(field, value);
                }
            }
            case LTE -> {
                if (andOr == AndOr.AND) {
                    dbTableQuery.andLte(field, value);
                } else {
                    dbTableQuery.orLte(field, value);
                }
            }
            case CUSTOM -> {
                if (value == null) {
                    value = new Object[]{};
                }
                if (andOr == AndOr.AND) {
                    if (value instanceof Object[] objects) {
                        dbTableQuery.and(field, objects);
                    } else {
                        dbTableQuery.and(field, value);
                    }
                } else {
                    if (value instanceof Object[] objects) {
                        dbTableQuery.or(field, objects);
                    } else {
                        dbTableQuery.or(field, value);
                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public enum QueryType {
        ORDER_BY(-1), EQ(0), LIKE(1), IN(2), NOT_IN(3), NEQ(4), GT(5), LT(6), GTE(7), LTE(8), CUSTOM(9);
        private final int value;

        QueryType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum AndOr {
        AND(0), OR(1);
        private final int value;

        AndOr(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
