package tech.powerjob.server.solon.core.lock;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.extension.LockService;
import tech.powerjob.server.solon.persistence.remote.model.OmsLockDO;
import tech.powerjob.server.solon.persistence.remote.repository.OmsLockRepository;
import tech.powerjob.common.utils.CommonUtils;
import tech.powerjob.common.utils.NetUtils;

/**
 * 基于数据库实现的分布式锁
 *
 * @author tjq
 * @since 2020/4/5
 */
@Slf4j
@Component
public class DatabaseLockService implements LockService, LifecycleBean {

    private String ownerIp;

    @Db
    private OmsLockRepository omsLockRepository;

    @Override
    public void start() {

        this.ownerIp = NetUtils.getLocalHost();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            int num = omsLockRepository.deleteByOwnerIP(ownerIp);
            log.info("[DatabaseLockService] execute shutdown hook, release all lock(owner={},num={})", ownerIp, num);
        }));
    }

    @Override
    public boolean tryLock(String name, long maxLockTime) {

        OmsLockDO newLock = new OmsLockDO(name, ownerIp, maxLockTime);
        try {
            omsLockRepository.insert(newLock);
            return true;
        } catch (Exception e) {
            log.warn("[DatabaseLockService] write lock to database failed, lockName = {}.", name, e);
        }

        OmsLockDO omsLockDO = omsLockRepository.findByLockName(name);
        long lockedMillions = System.currentTimeMillis() - LocalDateTimeUtil.toEpochMilli(omsLockDO.getUpdateTime());

        // 锁超时，强制释放锁并重新尝试获取
        if (lockedMillions > omsLockDO.getMaxLockTime()) {

            log.warn("[DatabaseLockService] The lock[{}] already timeout, will be unlocked now.", omsLockDO);
            unlock(name);
            return tryLock(name, maxLockTime);
        }
        return false;
    }

    @Override
    public void unlock(String name) {

        try {
            CommonUtils.executeWithRetry0(() -> omsLockRepository.deleteByLockName(name));
        } catch (Exception e) {
            log.error("[DatabaseLockService] unlock {} failed.", name, e);
        }
    }

}
