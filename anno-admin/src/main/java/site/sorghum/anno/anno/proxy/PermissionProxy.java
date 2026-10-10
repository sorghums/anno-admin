package site.sorghum.anno.anno.proxy;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermissionImpl;
import site.sorghum.anno.anno.functions.AnnoFunction;

/**
 * 许可代理
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Named
@Slf4j
public class PermissionProxy {

    public static final String VIEW = "view";

    public static final String VIEW_TRANSLATE = "查看";

    public static final String ADD = "add";

    public static final String ADD_TRANSLATE = "增加";

    public static final String UPDATE = "update";

    public static final String UPDATE_TRANSLATE = "修改";

    public static final String DELETE = "delete";

    public static final String DELETE_TRANSLATE = "删除";

    public void checkPermission(AnEntity anEntity, String code) {
        if (isSystemRun()) {
            return;
        }
        // 校验登录
        checkLogin();
        AnnoPermissionImpl annoPermission = anEntity.getAnnoPermission();
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        String permissionCode = StrUtil.isNotBlank(code) ? baseCode + ":" + code : baseCode;
        AnnoFunction.permissionCheckFunction.accept(permissionCode);
    }

    /**
     * 判断是否有权限(不抛异常，用于控制按钮/图表是否展示)
     * <p>
     * 以下情况视为“无需校验”，直接放行返回 true：
     * <ul>
     *     <li>系统运行中：无请求上下文，如定时任务、元数据初始化</li>
     *     <li>实体未开启权限：{@link AnnoPermissionImpl#enable()} 为 false</li>
     *     <li>未配置权限码：基础权限码或权限编码为空</li>
     * </ul>
     * 其余情况与 {@link #checkPermission(AnEntity, String)} 走同一套校验逻辑，
     * 未登录或无权限时返回 false，保证“展示”与“提交”的判定结果一致。
     *
     * @param anEntity 实体
     * @param code     权限编码
     * @return 有权限(或无需校验)返回 true
     */
    public boolean hasPermission(AnEntity anEntity, String code) {
        boolean systemRun;
        try {
            systemRun = isSystemRun();
        } catch (Exception e) {
            // 无请求上下文(例如定时任务)，按系统运行处理，避免元数据接口报错
            systemRun = true;
        }
        if (systemRun) {
            return true;
        }
        AnnoPermissionImpl annoPermission = anEntity.getAnnoPermission();
        boolean enable = annoPermission.enable();
        if (!enable) {
            return true;
        }
        String baseCode = annoPermission.baseCode();
        if (StrUtil.isBlank(baseCode) || StrUtil.isBlank(code)) {
            return true;
        }
        return checkPermissionQuietly(baseCode + ":" + code);
    }

    /**
     * 校验完整权限码，不抛异常
     * <p>
     * 与 {@link #checkPermission(AnEntity, String)} 使用同一个校验入口
     * {@link AnnoFunction#permissionCheckFunction}，因此用户自定义的权限校验实现同样生效。
     * 未登录、无权限以及校验过程抛出异常时均返回 false(展示侧宁可少展示)。
     *
     * @param permissionCode 完整权限码，如 an_user:view
     * @return 有权限返回 true
     */
    private boolean checkPermissionQuietly(String permissionCode) {
        try {
            // 校验登录
            checkLogin();
            // 校验权限
            AnnoFunction.permissionCheckFunction.accept(permissionCode);
            return true;
        } catch (Exception e) {
            log.debug("权限校验未通过, permissionCode={}", permissionCode, e);
            return false;
        }
    }

    public void checkLogin() {
        if (isSystemRun()) {
            return;
        }
        AnnoFunction.loginCheckFunction.run();
    }

    private boolean isSystemRun() {
        SaRequest request = SaHolder.getRequest();
        return request == null || request.getSource() == null;
    }


}
