package anno.method;

import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.MethodTemplateManager;

import java.io.IOException;

/**
 * @author songyinyin
 * @since 2024/1/16 12:25
 */
public class MethodBasicProcessorTest {

    @Test
    public void test() throws IOException {
        MethodTemplateManager.clear();
        try (MockedStatic<AnnoBeanUtils> mocked = Mockito.mockStatic(AnnoBeanUtils.class)){
            mocked.when(() -> AnnoBeanUtils.getBean(StrUtil.lowerFirst(TestBeanProcessor.class.getSimpleName()))).thenReturn(new TestBeanProcessor());
            mocked.when(() -> AnnoBeanUtils.getBean(StrUtil.lowerFirst(TestMethodServiceImpl.class.getSimpleName()))).thenReturn(new TestMethodServiceImpl());
            AnnoProperty annoProperty = new AnnoProperty();
            annoProperty.setDetailLogThreshold(0);
            mocked.when(() -> AnnoBeanUtils.getBean(AnnoProperty.class)).thenReturn(annoProperty);

            MethodTemplateManager.parse("anno.method");
            TestMethodService testMethodService = MethodTemplateManager.create(TestMethodService.class);
            DbCriteria criteria = new DbCriteria();
            criteria.setEntityName(TestUser.class.getSimpleName());
            testMethodService.delete(criteria);

            Assert.assertEquals("_Processor_Bean", criteria.getTableName());
        }

    }

}
