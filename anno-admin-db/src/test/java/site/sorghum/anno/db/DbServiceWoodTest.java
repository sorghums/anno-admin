package site.sorghum.anno.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import org.noear.wood.DbContext;
import org.noear.wood.IPage;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.db.app.entity.TestEntity;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.RemoveParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno.ddl.entity2db.SampleEntityToTableGetter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据库服务Wood测试
 *
 * @author sorghum
 * @since 2023/07/08
 */
@Slf4j
@SolonTest(TestApp.class)
@RunWith(SolonJUnit4ClassRunner.class)
public class DbServiceWoodTest {


    @Inject("dbServiceWood")
    DbService dbService;
    @Db
    DbContext dbContext;

    TableParam<TestEntity> testEntityTableParam = new TableParam<>() {{
        setClazz(TestEntity.class);
        setTableName("test_entity");
        setRemoveParam(new RemoveParam() {{
            setLogic(false);
        }});
    }};

    PageParam pageParam = new PageParam() {{
        setPage(1);
        setLimit(10);
    }};

    @Before
    public void init() {
        EntityToDdlGenerator<Class<?>> generator = new EntityToDdlGenerator<>(dbContext, new SampleEntityToTableGetter());
        generator.autoMaintainTable(TestEntity.class);
    }


    @Test
    public void testCase1() {
        long insert = dbService.insert(
            testEntityTableParam,
            new TestEntity() {{
                setId("1000");
                setBigDecimalNum(new BigDecimal(12));
                setIntegerNum(10);
            }}
        );
        ArrayList<DbCondition> conditions = new ArrayList<>();
        conditions.add(
            DbCondition.builder().field("id").value("1000").build()
        );
        TestEntity entity = dbService.queryOne(testEntityTableParam, conditions);
        assert entity.getId().equals("1000");

        dbService.update(
            testEntityTableParam,
            conditions,
            new TestEntity() {{
                setIntegerNum(12);
            }}
        );
        entity = dbService.queryOne(testEntityTableParam, conditions);
        assert entity.getIntegerNum() == 12;


        IPage<TestEntity> page = dbService.page(testEntityTableParam, Collections.emptyList(), pageParam);
        assert page.getList().size() == 1;

        List<TestEntity> list = dbService.list(testEntityTableParam, Collections.emptyList());
        assert list.size() == 1;

        dbService.delete(testEntityTableParam, conditions);

        entity = dbService.queryOne(testEntityTableParam, conditions);
        assert entity == null;
    }


}
