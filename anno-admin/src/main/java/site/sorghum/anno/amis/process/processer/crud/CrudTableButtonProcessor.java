package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnTableButton;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudTableButtonProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Crud crudBody = crudView.getCrudBody();
        Form filter = crudBody.getFilter();
        List<Action> actions = filter.getActions();
        List<AnTableButton> anButtons = anEntity.getTableButtons();
        for (AnTableButton anButton : anButtons) {
            try {
                permissionProxy.checkPermission(anEntity, anButton.getPermissionCode());
            } catch (Exception e) {
                continue;
            }
            Action action = new Action();
            if (StrUtil.isNotBlank(anButton.getJumpUrl())) {
                action.setLabel(anButton.getName());
                action.setActionType("url");
                action.setUrl(anButton.getJumpUrl());
            } else if (StrUtil.isNotBlank(anButton.getJsCmd())) {
                action.setLabel(anButton.getName());
                action.setOnClick(anButton.getJsCmd());
            } else if (anButton.isJavaCmdEnable()) {
                action.setLabel(anButton.getName());
                action.setActionType("ajax");
                action.setApi(
                        new Api() {{
                            setMethod("post");
                            setUrl("/amis/system/anno/runJavaCmd");
                            setData(new HashMap<>() {{
                                put("clazz", CryptoUtil.encrypt(anButton.getJavaCmdBeanClass().getName()));
                                put("method", CryptoUtil.encrypt(anButton.getJavaCmdMethodName()));
                                // 30分钟过期
                                put("expireTime", CryptoUtil.encrypt(String.valueOf(System.currentTimeMillis() + 30 * 60 * 1000)));
                                put("&", "$$");
                            }});
                            setMessages(
                                    new ApiMessage() {{
                                        setSuccess("操作成功");
                                        setFailed("操作失败");
                                    }}
                            );

                        }}
                );
            } else {
                continue;
            }
            actions.add(action);
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
