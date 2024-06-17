package tech.powerjob.server.solon.persistence.remote.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.db.BaseMetaModel;

import java.time.LocalDateTime;

/**
 * 容器（jar容器）信息表
 *
 * @author tjq
 * @since 2020/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "容器（jar容器）信息表")
@Table("pj_container_info")
public class ContainerInfoDO extends BaseMetaModel {

    /**
     * 所属的应用ID
     */
    @AnnoField(title = "所属的应用ID")
    private String appId;

    @AnnoField(title = "容器名称")
    private String containerName;

    /**
     * 容器类型，枚举值为 ContainerSourceType
     */
    @AnnoField(title = "容器类型，枚举值为 ContainerSourceType")
    private Integer sourceType;
    /**
     * 由 sourceType 决定，JarFile -> String，存储文件名称；Git -> JSON，包括 URL，branch，username，password
     */
    @AnnoField(title = "应用信息")
    private String sourceInfo;

    /**
     * 版本 （Jar包使用md5，Git使用commitId，前者32位，后者40位，不会产生碰撞）
     */
    @AnnoField(title = "版本")
    private String version;

    /**
     * 状态，枚举值为 ContainerStatus
     */
    @AnnoField(title = "状态，枚举值为 ContainerStatus")
    private Integer status;

    /**
     * 上一次部署时间
     */
    @AnnoField(title = "上一次部署时间")
    private LocalDateTime lastDeployTime;

}
