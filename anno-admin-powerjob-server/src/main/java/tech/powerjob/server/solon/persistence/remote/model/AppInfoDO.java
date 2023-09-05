package tech.powerjob.server.solon.persistence.remote.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.pre.plugin.proxy.SysRoleProxy;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

/**
 * 应用信息表
 *
 * @author tjq
 * @since 2020/3/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "应用信息",
    annoPermission = @AnnoPermission(enable = true, baseCode = "pj_app_info", baseCodeTranslate = "应用信息"),
    annoOrder = @AnnoOrder(orderValue = "appName"))
@Table("pj_app_info")
public class AppInfoDO extends BaseMetaModel {

    @AnnoField(title = "应用名称", search = @AnnoSearch(),
        edit = @AnnoEdit(placeHolder = "请输入应用名称", notNull = true))
    private String appName;

    /**
     * 应用分组密码
     */
    @AnnoField(title = "应用分组密码", edit = @AnnoEdit(placeHolder = "请输入密码", notNull = true, editEnable = false), show = false)
    private String password;

    /**
     * 当前负责该 appName 旗下任务调度的server地址，IP:Port（注意，该地址为ActorSystem地址，而不是HTTP地址，两者端口不同）
     * 支持多语言后，尽管引入了 vert.x 的地址，但该字段仍保存 ActorSystem 的地址，vert.x 地址仅在返回给 worker 时特殊处理
     * 原因：框架中很多地方强依赖 currentServer，比如根据该地址来获取需要调度的 app
     */
    @AnnoField(title = "当前调度的 Server 地址")
    private String currentServer;

}
