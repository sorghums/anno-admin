package anno.method;

import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.processor.MTBasicProcessor;
import site.sorghum.anno.method.MTContext;
import site.sorghum.anno.method.MTProcessResult;

/**
 * @author songyinyin
 * @since 2024/1/17 00:02
 */
public class TestBeanProcessor implements MTBasicProcessor {
    @Override
    public MTProcessResult process(MTContext context) throws Exception {
        Thread.sleep(10);
        DbCriteria criteria = (DbCriteria) context.getArgs()[0];
        String tableName = criteria.getTableName();
        if (tableName == null) {
            tableName = "_Processor";
        } else {
            tableName += "_Processor";
        }
        criteria.setTableName(tableName);
        return MTProcessResult.success();
    }
}
