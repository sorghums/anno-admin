package site.sorghum.anno.modular.amis.process.processer.crud;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno.common.util.CryptoUtil;
import site.sorghum.anno.metadata.AnButton;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.proxy.PermissionProxy;

import java.util.ArrayList;
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
public class CrudColumnButtonProcessor implements BaseProcessor {

  @Inject
  MetadataManager metadataManager;

  @Inject
  PermissionProxy permissionProxy;

  @Override
  public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
    if (properties.getOrDefault("isM2m", false).equals(true)) {
      chain.doProcessor(amisBaseWrapper, clazz, properties);
      return;
    }
    CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
    AnEntity anEntity = metadataManager.getEntity(clazz);
    // 读取现有的列
    Crud crudBody = crudView.getCrudBody();
    List<Map> columns = crudBody.getColumns();
    for (Map columnJson : columns) {
      if ("操作".equals(MapUtil.getStr(columnJson, "label"))) {
        List<AnButton> anButtons = anEntity.getButtons();
        for (AnButton anButton : anButtons) {
          try {
            permissionProxy.checkPermission(anEntity, anButton.getPermissionCode());
          } catch (Exception e) {
            continue;
          }
          Action action = new Action();
          if (anButton.isO2mEnable()) {
            action = new DrawerButton();
            action.setLabel(anButton.getName());
            ((DrawerButton) action).setDrawer(
                new DrawerButton.Drawer() {{
                  setShowCloseButton(false);
                  setPosition("right");
                  setCloseOnOutside(true);
                  setSize("xl");
                  setHeaderClassName("p-none m-none h-0");
                  setFooterClassName("p-xs m-xs h-1/2");
                  setActions(new ArrayList<>());
                  setBody(
                      new IFrame() {{
                        setType("iframe");
                        setSrc("/index#/amisSingle/index/" + anButton.getO2mJoinMainClazz().getSimpleName() + "?" + anButton.getO2mJoinOtherField() + "=${" + anButton.getO2mJoinThisField() + "}");
                      }}
                  );
                }}
            );
          } else if (anButton.isM2mEnable()) {
            action = new DrawerButton();
            HashMap<String, Object> queryMap = new HashMap<String, Object>() {{
              put("joinValue", "${" + anButton.getM2mJoinThisClazzField() + "}");
              put("joinCmd", Base64.encodeStr(anButton.getM2mJoinSql().getBytes(), false, true));
              // 处理上调换this和other的逻辑
              put("mediumThisField", anButton.getM2mMediumOtherField());
              put("mediumOtherField", anButton.getM2mMediumThisField());
              put("mediumTableClass", anButton.getM2mMediumTableClass().getSimpleName());
              put("joinThisClazzField", anButton.getM2mJoinThisClazzField());
              put("isM2m", true);
            }};
            action.setLabel(anButton.getName());
            ((DrawerButton) action).setDrawer(
                new DrawerButton.Drawer() {{
                  setCloseOnEsc(true);
                  setCloseOnOutside(true);
                  setSize("xl");
                  setShowCloseButton(false);
                  setHeaderClassName("p-none m-none h-0");
                  setFooterClassName("p-xs m-xs h-1/2");
                  setBody(
                      new IFrame() {{
                        setType("iframe");
                        setSrc("/index#/amisSingle/index/" + anButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
                      }}
                  );
                  setActions(new ArrayList<Action>());
                }}
            );
          } else if (StrUtil.isNotBlank(anButton.getJumpUrl())) {
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
                  setUrl("/system/anno/runJavaCmd");
                  setData(new HashMap<String, Object>() {{
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
          // 添加对应按钮
          Object buttons = columnJson.get("buttons");
          if (buttons instanceof List<?> buttonList) {
            List<Object> buttonListMap = (List<Object>) buttonList;
            buttonListMap.add(action);
            // 设置列宽
            columnJson.put("width", buttonListMap.size() * 80);
          }
        }
      }
    }
    chain.doProcessor(amisBaseWrapper, clazz, properties);
  }
}
