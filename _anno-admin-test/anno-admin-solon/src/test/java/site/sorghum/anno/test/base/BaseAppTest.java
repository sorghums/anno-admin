package site.sorghum.anno.test.base;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.test.AnnoSolonAdminStarter;

/**
 * @author songyinyin
 * @since 2023/11/11 22:20
 */
@SolonTest(value = AnnoSolonAdminStarter.class, env = "dev")
@RunWith(SolonJUnit4ClassRunner.class)
public class BaseAppTest {

    protected MockedStatic<AnnoStpUtil> mocked;

    @Before
    public void before() {
        mocked = Mockito.mockStatic(AnnoStpUtil.class);
        mocked.when(AnnoStpUtil::getLoginId).thenReturn("1666356287765979136");
    }

    @After
    public void after() {
        mocked.close();
    }
}
