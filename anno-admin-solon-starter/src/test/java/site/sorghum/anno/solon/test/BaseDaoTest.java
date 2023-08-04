package site.sorghum.anno.solon.test;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.pre.plugin.ao.SysUser;
import site.sorghum.anno.pre.plugin.dao.SysUserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author songyinyin
 * @since 2023/8/4 18:44
 */
@Slf4j
@SolonTest(TestApp.class)
@RunWith(SolonJUnit4ClassRunner.class)
public class BaseDaoTest {

    @Db
    SysUserDao userDao;

    @Test
    public void testBatchAdd() {
        userDao.delete(m -> m.whereLk("name", "testUser_%"));

        List<SysUser> uerList = getUerList(1000);
        StopWatch stopWatch = new StopWatch("add");
        stopWatch.start("batchAdd 1000");
        userDao.insertList(uerList);
        stopWatch.stop();
        log.info("{}", stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

        Long count = userDao.selectCount(m -> m.whereLk("name", "testUser_%"));
        Assert.assertEquals(1000L, count.longValue());
    }

    private List<SysUser> getUerList(int size) {
        List<SysUser> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            SysUser user = new SysUser();
            user.setName("testUser_" + i);
            list.add(user);
        }
        return list;
    }
}
