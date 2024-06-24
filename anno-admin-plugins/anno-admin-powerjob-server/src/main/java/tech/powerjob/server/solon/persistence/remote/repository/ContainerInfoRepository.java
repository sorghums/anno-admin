package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;
import tech.powerjob.server.solon.persistence.remote.model.ContainerInfoDO;

import java.util.List;

/**
 * 容器信息 数据操作层
 *
 * @author tjq
 * @since 2020/5/15
 */
@Namespace("powerjob.server")
public interface ContainerInfoRepository extends AnnoBaseMapper<ContainerInfoDO> {

    @Sql("select * from pj_container_info where app_id = ? and status != ?")
    List<ContainerInfoDO> findByAppIdAndStatusNot(String appId, Integer status);
}
