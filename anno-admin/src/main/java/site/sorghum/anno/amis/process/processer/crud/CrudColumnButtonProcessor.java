package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.PermissionProxy;

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
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        for (AnColumnButton anColumnButton : anColumnButtons) {
          try {
            permissionProxy.checkPermission(anEntity, anColumnButton.getPermissionCode());
          } catch (Exception e) {
            continue;
          }
          Action action = new Action();
          if (anColumnButton.isO2mEnable()) {
            action = new DialogButton();
            action.setLabel(anColumnButton.getName());
            ((DialogButton) action).setDialog(
                new DialogButton.Dialog() {{
                  setTitle(anColumnButton.getName());
                  setShowCloseButton(true);
                  setSize(anColumnButton.getO2mWindowSize());
                  setActions(new ArrayList<>());
                  setBody(
                      new IFrame() {{
                        setType("iframe");
                        setHeight(anColumnButton.getO2mWindowHeight());
                        setSrc("/index.html#/amisSingle/index/" + anColumnButton.getO2mJoinMainClazz().getSimpleName() + "?" + anColumnButton.getO2mJoinOtherField() + "=${" + anColumnButton.getO2mJoinThisField() + "}");
                      }}
                  );
                }}
            );
          } else if (anColumnButton.isM2mEnable()) {
            action = new DialogButton();
            HashMap<String, Object> queryMap = new HashMap<>() {{
                put("joinValue", "${" + anColumnButton.getM2mJoinThisClazzField() + "}");
                put("joinCmd", Base64.encodeStr(anColumnButton.getM2mJoinSql().getBytes(), false, true));
                // 处理上调换this和other的逻辑
                put("mediumThisField", anColumnButton.getM2mMediumOtherField());
                put("mediumOtherField", anColumnButton.getM2mMediumThisField());
                put("mediumTableClass", anColumnButton.getM2mMediumTableClass().getSimpleName());
                put("joinThisClazzField", anColumnButton.getM2mJoinThisClazzField());
                put("isM2m", true);
            }};
            action.setLabel(anColumnButton.getName());
            ((DialogButton) action).setDialog(
                new DialogButton.Dialog() {{
                  setCloseOnEsc(true);
                  setTitle(anColumnButton.getName());
                  setSize(anColumnButton.getM2mWindowSize());
                  setShowCloseButton(true);
                  setBody(
                      new IFrame() {{
                        setType("iframe");
                        setHeight(anColumnButton.getM2mWindowHeight());
                        setSrc("/index.html#/amisSingle/index/" + anColumnButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
                      }}
                  );
                  setActions(new ArrayList<>());
                }}
            );
          } else if (StrUtil.isNotBlank(anColumnButton.getJumpUrl())) {
            action.setLabel(anColumnButton.getName());
            action.setActionType("url");
            action.setUrl(anColumnButton.getJumpUrl());
          } else if (StrUtil.isNotBlank(anColumnButton.getJsCmd())) {
            action.setLabel(anColumnButton.getName());
            action.setOnClick(anColumnButton.getJsCmd());
          } else if (anColumnButton.isJavaCmdEnable()) {
            action.setLabel(anColumnButton.getName());
            action.setActionType("ajax");
            action.setApi(
                new Api() {{
                  setMethod("post");
                  setUrl("/amis/system/anno/runJavaCmd");
                  setData(new HashMap<>() {{
                      put("clazz", CryptoUtil.encrypt(anColumnButton.getJavaCmdBeanClass().getName()));
                      put("method", CryptoUtil.encrypt(anColumnButton.getJavaCmdMethodName()));
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
