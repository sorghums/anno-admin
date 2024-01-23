package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._ddl.InitDataService;
import site.sorghum.anno.plugin.ao.AnSql;
import site.sorghum.anno.plugin.dao.AnSqlDao;
import site.sorghum.anno.plugin.service.AnSqlService;

import java.net.URL;
import java.util.Map;

@Named
@Slf4j
public class AnSqlServiceImpl implements AnSqlService {

    @Db
    AnSqlDao anSqlDao;
    @Inject
    InitDataService initDataService;

    @Override
    public void runSql(Map<String, Object> data) {
        AnSql anSql = anSqlDao.findById(data.get("id")).get();

        String fileName = (String) data.get("version");
        URL resource = ResourceUtil.getResource("init-data/" + fileName);
        if (FileUtil.file(resource).length() <= 0) {
            throw new RuntimeException(fileName + "内容为空！");
        }
        try {
            initDataService.init(resource);
            anSql.setState(1);
        } catch (Exception e) {
            anSql.setState(2);
            throw new RuntimeException("手动执行" + fileName + "失败 => " + e);
        } finally {
            anSql.setRunTime(DateUtil.date());
            anSqlDao.updateById(anSql);
        }
    }
}
