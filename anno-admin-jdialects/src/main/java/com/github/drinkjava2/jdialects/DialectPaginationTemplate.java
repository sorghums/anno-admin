/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.drinkjava2.jdialects;

/**
 * SQL pagination templates, includes 2 kind of paigin template: sqlTemplate and topLimitTemplate
 * 
 * @author Yong Zhu
 * @since 1.0.1
 */
@SuppressWarnings("all")
public class DialectPaginationTemplate {
    
    protected static void initPaginTemplates() {
        for (Dialect d : Dialect.dialects) {
            d.sqlTemplate = initializePaginSQLTemplate(d);
            d.topLimitTemplate = initializeTopLimitSqlTemplate(d);
        }
    }
    
    
    /**
    * Return pagination template of this Dialect     
    */
    protected static String initializePaginSQLTemplate(Dialect d) {
        switch (DialectType.getDialectType(d)){
        case Cache71Dialect:
        case DB2390Dialect:
        case DB2390V8Dialect:
        case FrontBaseDialect:
        case InformixDialect:
        case IngresDialect:
        case JDataStoreDialect:
        case MckoiDialect:
        case MimerSQLDialect:
        case PointbaseDialect:
        case ProgressDialect:
        case RDMSOS2200Dialect:
        case SAPDBDialect:
        case SQLServerDialect:
        case Sybase11Dialect:
        case SybaseAnywhereDialect:
        case SybaseASE15Dialect:
        case SybaseDialect:
        case Teradata14Dialect:
        case TeradataDialect:
        case TimesTenDialect:
            return Dialect.NOT_SUPPORT;
        case DamengDialect:
            return "select $BODY limit $PAGESIZE offset $SKIP_ROWS ";
        case H2Dialect:
        case HANAColumnStoreDialect:
        case HANARowStoreDialect:
        case PostgresPlusDialect:
        case PostgreSQL81Dialect:
        case PostgreSQL82Dialect:
        case PostgreSQL91Dialect:
        case PostgreSQL92Dialect:
        case PostgreSQL93Dialect:
        case PostgreSQL94Dialect:
        case PostgreSQL95Dialect:
        case PostgreSQL9Dialect:
        case PostgreSQLDialect:
        case SQLiteDialect:
            return "select $BODY limit $PAGESIZE offset $SKIP_ROWS";
        case AccessDialect:
        case CobolDialect:
        case CUBRIDDialect:
        case DbfDialect:
        case ExcelDialect:
        case MariaDB102Dialect:
        case MariaDB103Dialect:
        case MariaDB10Dialect:
        case MariaDB53Dialect:
        case MariaDBDialect:
        case MySQL55Dialect:
        case MySQL57Dialect:
        case MySQL57InnoDBDialect:
        case MySQL5Dialect:
        case MySQL5InnoDBDialect:
        case MySQL8Dialect:
        case MySQLDialect:
        case MySQLInnoDBDialect:
        case MySQLMyISAMDialect:
        case ParadoxDialect:
        case TextDialect:
        case XMLDialect:
            return "select $BODY limit $SKIP_ROWS, $PAGESIZE";
        case Ingres10Dialect:
        case Ingres9Dialect:
            return "select $BODY offset $skip_rows fetch first $pagesize rows only";
        case DerbyDialect:
        case DerbyTenFiveDialect:
        case DerbyTenSevenDialect:
        case DerbyTenSixDialect:
            return "select $BODY offset $skip_rows rows fetch next $pagesize rows only";
        case SQLServer2012Dialect:
            return "select $BODY offset $SKIP_ROWS rows fetch next $PAGESIZE rows only";
        case InterbaseDialect:
            return "select $BODY rows $SKIP_ROWS to $PAGESIZE";
        case SybaseASE157Dialect:
            return "select ($DISTINCT) top $total_rows $BODY";
        case DB2400Dialect:
        case DB297Dialect:
        case DB2Dialect:
            return "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( select $BODY fetch first $total_rows rows only ) as inner2_ ) as inner1_ where rownumber_ > $skip_rows order by rownumber_";
        case GBaseDialect:
        case Oracle8iDialect:
        case OracleDialect:
            return "select * from ( select row_.*, rownum rownum_ from ( select $BODY ) row_ ) where rownum_ <= $TOTAL_ROWS and rownum_ > $SKIP_ROWS";
        case DataDirectOracle9Dialect:
        case Oracle10gDialect:
        case Oracle12cDialect:
        case Oracle9Dialect:
        case Oracle9iDialect:
            return "select * from ( select row_.*, rownum rownum_ from ( select $BODY ) row_ where rownum <= $TOTAL_ROWS) where rownum_ > $SKIP_ROWS";
        case FirebirdDialect:
            return "select first $PAGESIZE skip $SKIP_ROWS $BODY";
        case HSQLDialect:
            return "select limit $SKIP_ROWS $PAGESIZE $BODY";
        case Informix10Dialect:
            return "select SKIP $skip_rows first $pagesize $BODY";
        case SQLServer2005Dialect:
        case SQLServer2008Dialect:
            return "WITH query AS (SELECT TMP_.*, ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as ROW_NUM_ FROM ( select ($DISTINCT) TOP($TOTAL_ROWS) $BODY ) TMP_ ) SELECT * FROM query WHERE ROW_NUM_ >$SKIP_ROWS AND ROW_NUM_ <= $TOTAL_ROWS";
        default:
            return Dialect.NOT_SUPPORT;
        }
    }

    /**
     * Return top limit sql template of this Dialect
    */
    protected static String initializeTopLimitSqlTemplate(Dialect d) {
        switch (DialectType.getDialectType(d)){
        case FrontBaseDialect:
        case JDataStoreDialect:
        case MckoiDialect:
        case MimerSQLDialect:
        case PointbaseDialect:
        case ProgressDialect:
        case SAPDBDialect:
        case Sybase11Dialect:
        case SybaseAnywhereDialect:
        case SybaseASE15Dialect:
        case SybaseDialect:
        case Teradata14Dialect:
        case TeradataDialect:
            return Dialect.NOT_SUPPORT;
        case DB2390Dialect:
        case DB2390V8Dialect:
        case DB2400Dialect:
        case DB297Dialect:
        case DB2Dialect:
        case DerbyDialect:
        case DerbyTenFiveDialect:
        case DerbyTenSevenDialect:
        case DerbyTenSixDialect:
        case Ingres10Dialect:
        case Ingres9Dialect:
            return "select $BODY fetch first $pagesize rows only";
        case RDMSOS2200Dialect:
            return "select $BODY fetch first $pagesize rows only ";
        case AccessDialect:
        case CobolDialect:
        case CUBRIDDialect:
            return "select $BODY limit $PAGESIZE";
        case DamengDialect:
            return "select $BODY limit $PAGESIZE ";
        case DbfDialect:
        case ExcelDialect:
        case H2Dialect:
        case HANAColumnStoreDialect:
        case HANARowStoreDialect:
        case MariaDB102Dialect:
        case MariaDB103Dialect:
        case MariaDB10Dialect:
        case MariaDB53Dialect:
        case MariaDBDialect:
        case MySQL55Dialect:
        case MySQL57Dialect:
        case MySQL57InnoDBDialect:
        case MySQL5Dialect:
        case MySQL5InnoDBDialect:
        case MySQL8Dialect:
        case MySQLDialect:
        case MySQLInnoDBDialect:
        case MySQLMyISAMDialect:
        case ParadoxDialect:
        case PostgresPlusDialect:
        case PostgreSQL81Dialect:
        case PostgreSQL82Dialect:
        case PostgreSQL91Dialect:
        case PostgreSQL92Dialect:
        case PostgreSQL93Dialect:
        case PostgreSQL94Dialect:
        case PostgreSQL95Dialect:
        case PostgreSQL9Dialect:
        case PostgreSQLDialect:
        case SQLiteDialect:
        case TextDialect:
        case XMLDialect:
            return "select $BODY limit $PAGESIZE";
        case SQLServer2012Dialect:
            return "select $BODY offset 0 rows fetch next $PAGESIZE rows only";
        case InterbaseDialect:
            return "select $BODY rows $PAGESIZE";
        case Cache71Dialect:
            return "select ($DISTINCT) top $PAGESIZE $BODY";
        case SQLServerDialect:
        case SybaseASE157Dialect:
            return "select ($DISTINCT) top $pagesize $BODY";
        case SQLServer2005Dialect:
        case SQLServer2008Dialect:
            return "select ($DISTINCT) TOP($PAGESIZE) $BODY";
        case DataDirectOracle9Dialect:
        case GBaseDialect:
        case Oracle10gDialect:
        case Oracle12cDialect:
        case Oracle8iDialect:
        case Oracle9Dialect:
        case Oracle9iDialect:
        case OracleDialect:
            return "select * from ( select $BODY ) where rownum <= $PAGESIZE";
        case FirebirdDialect:
            return "select first $PAGESIZE $BODY";
        case Informix10Dialect:
        case InformixDialect:
        case IngresDialect:
        case TimesTenDialect:
            return "select first $pagesize $BODY";
        case HSQLDialect:
            return "select top $PAGESIZE $BODY";
        default:
            return Dialect.NOT_SUPPORT;
        }
    }

}
