package anno.method;

import site.sorghum.anno.db.DbCriteria;

/**
 * @author songyinyin
 * @since 2024/1/17 00:02
 */
public class TestMethodServiceImpl implements TestMethodService {
    @Override
    public void delete(DbCriteria criteria) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String tableName = criteria.getTableName();
        if (tableName == null) {
            tableName = "_Bean";
        } else {
            tableName += "_Bean";
        }
        criteria.setTableName(tableName);
    }
}
