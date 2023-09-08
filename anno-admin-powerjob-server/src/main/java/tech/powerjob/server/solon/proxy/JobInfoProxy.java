package tech.powerjob.server.solon.proxy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import tech.powerjob.common.enums.DispatchStrategy;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.model.LifeCycle;
import tech.powerjob.common.model.LogConfig;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.core.scheduler.TimingStrategyService;
import tech.powerjob.server.solon.core.service.impl.job.JobServiceImpl;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/6 13:56
 */
@Component
public class JobInfoProxy implements AnnoBaseProxy<JobInfoDO> {

    @Inject
    JobServiceImpl jobService;
    @Inject
    TimingStrategyService timingStrategyService;

    @Override
    public void beforeAdd(JobInfoDO data) {
        fillDefaultValue(data);
        processVirtualColumn(data);
        validAndCalculate(data);
    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, JobInfoDO data) {
        fillDefaultValue(data);
        processVirtualColumn(data);
        validAndCalculate(data);
    }

    @Override
    public void afterFetch(Class<JobInfoDO> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<JobInfoDO> page) {
        for (JobInfoDO data : page.getList()) {
            String logConfig = data.getLogConfig();
            if (StrUtil.isNotBlank(logConfig)) {
                LogConfig config = JSON.parseObject(logConfig, LogConfig.class);
                data.setLevel(config.getLevel());
                data.setType(config.getType());
                data.setLoggerName(config.getLoggerName());
            }
        }
    }

    private void processVirtualColumn(JobInfoDO data) {
        LogConfig logConfig = new LogConfig();
        logConfig.setLevel(data.getLevel());
        logConfig.setType(data.getType());
        logConfig.setLoggerName(data.getLoggerName());
        data.setLogConfig(JSON.toJSONString(logConfig));

        LifeCycle lifeCycle = new LifeCycle();
        lifeCycle.setStart(toTime(data.getLifecycleStart()));
        lifeCycle.setEnd(toTime(data.getLifecycleEnd()));
        data.setLifecycle(JSON.toJSONString(lifeCycle));
    }

    private void fillDefaultValue(JobInfoDO data) {
        jobService.fillDefaultValue(data);
        if (data.getMaxInstanceNum() == null) {
            data.setMaxInstanceNum(0);
        }
        if (data.getConcurrency() == null) {
            data.setConcurrency(5);
        }
        if (data.getInstanceTimeLimit() == null) {
            data.setInstanceTimeLimit(0L);
        }
        if (data.getInstanceRetryNum() == null) {
            data.setInstanceRetryNum(0);
        }
        if (data.getTaskRetryNum() == null) {
            data.setTaskRetryNum(0);
        }
        if (data.getMinCpuCores() == null) {
            data.setMinCpuCores(0.0);
        }
        if (data.getMinMemorySpace() == null) {
            data.setMinMemorySpace(0.0);
        }
        if (data.getMinDiskSpace() == null) {
            data.setMinDiskSpace(0.0);
        }
        if (data.getDispatchStrategy() == null) {
            data.setDispatchStrategy(DispatchStrategy.HEALTH_FIRST.getV());
        }
        if (data.getStatus() == null) {
            data.setStatus(SwitchableStatus.ENABLE.getV());
        }
    }

    private void validAndCalculate(JobInfoDO data) {
        // 检查定时策略
        timingStrategyService.validate(TimeExpressionType.of(data.getTimeExpressionType()), data.getTimeExpression(),
            toTime(data.getLifecycleStart()), toTime(data.getLifecycleEnd()));
        jobService.calculateNextTriggerTime(data);
    }

    private Long toTime(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return LocalDateTimeUtil.toEpochMilli(time);
    }
}
