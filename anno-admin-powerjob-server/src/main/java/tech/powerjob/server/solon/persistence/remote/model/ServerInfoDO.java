package tech.powerjob.server.solon.persistence.remote.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

/**
 * 服务器信息表（用于分配服务器唯一ID）
 *
 * @author tjq
 * @since 2020/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "服务器信息表")
@NoArgsConstructor
@Table("pj_server_info")
public class ServerInfoDO extends BaseMetaModel {

    /**
     * 服务器IP地址
     */
    @AnnoField(title = "服务器ip地址")
    private String ip;

    public ServerInfoDO(String ip) {
        this.ip = ip;
    }
}
