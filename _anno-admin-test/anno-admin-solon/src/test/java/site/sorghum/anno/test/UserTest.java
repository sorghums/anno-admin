package site.sorghum.anno.test;

import org.junit.Test;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.test.base.BaseAppTest;

/**
 * @author songyinyin
 * @since 2023/11/11 22:21
 */
public class UserTest extends BaseAppTest{

    @Inject
    DbServiceWithProxy dbServiceWithProxy;

    @Db
    DbContext dbContext;

    @Test
    @Tran
    public void testAdd() throws Exception {
        dbContext.table("an_user").where("mobile=?", "18800000009").delete();


        AnUser user = new AnUser();
        user.setMobile("18800000009");
        user.setName("test-009");
        user.setEnable("0");
        user.setOrgId("abc");
        user.setPassword("123456");
        dbServiceWithProxy.insert(user);

    }
}
