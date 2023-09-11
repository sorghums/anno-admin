package site.sorghum.anno.amis.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.noear.wood.DbContext;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.amis.entity.layout.Tabs;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AMIS 常用方法集合
 *
 * @author Sorghum
 * @since 2023/09/08
 */
public class AmisCommonUtil {

    public static List<AmisBase> formItemToGroup(AnEntity entity, List<AmisBase> formItems, Integer columnCnt) {
        String[] ignoreTypes = {
            "editor", "input-rich-text", "input-image"
        };
        // 加入传入的忽略类型
        Map<Integer, List<AmisBase>> indexMap = new HashMap<>();
        List<AmisBase> rst = new ArrayList<>();
        int idx = 0;
        for (AmisBase formItem : formItems) {
            if (Boolean.TRUE.equals(formItem.getHidden())) {
                if (formItem instanceof FormItem) {
                    // 主键需要隐藏在页面上，后续会用到
                    String name = ((FormItem) formItem).getName();
                    AnField field = entity.getField(name);
                    if (field != null && field.isPrimaryKey()) {
                        rst.add(formItem);
                    }
                }
                continue;
            }
            List<AmisBase> nowIdxFormItems = indexMap.computeIfAbsent(idx, k -> new ArrayList<>());
            if (nowIdxFormItems.size() == columnCnt) {
                idx++;
                nowIdxFormItems = indexMap.computeIfAbsent(idx, k -> new ArrayList<>());
            }
            // 如果是忽略类型则单独放到一个组中
            boolean skip = false;
            for (String ignoreType : ignoreTypes) {
                if (ignoreType.equals(formItem.getType())) {
                    if (!nowIdxFormItems.isEmpty()) {
                        idx++;
                        nowIdxFormItems = indexMap.computeIfAbsent(idx, k -> new ArrayList<>());
                    }
                    nowIdxFormItems.add(formItem);
                    idx++;
                    skip = true;
                }
            }
            if (skip) {
                continue;
            }
            // 如果不是formItem则单独放到一个组中
            if (!(formItem instanceof FormItem)) {
                if (!nowIdxFormItems.isEmpty()) {
                    idx++;
                    nowIdxFormItems = indexMap.computeIfAbsent(idx, k -> new ArrayList<>());
                }
                nowIdxFormItems.add(formItem);
                idx++;
                continue;
            }
            nowIdxFormItems.add(formItem);
        }
        // 转到rst中
        int size = indexMap.size();
        for (int i = 0; i < size; i++) {
            Group group = new Group();
            group.setBody(indexMap.get(i));
            rst.add(group);
        }
        return rst;
    }

    /**
     * 获取下拉选的数据
     *
     * @return key=AnField.OptionData.value（存到数据库中的数据）, value=AnField.OptionData.label（中文意思）
     */
    public static Map<Object, Object> getOptionData(AnField anField) {
        HashMap<Object, Object> mapping = new HashMap<>();
        String optionTypeSql = anField.getOptionTypeSql();
        AnField.OptionAnnoClass optionAnnoClass = anField.getOptionAnnoClass();
        if (StrUtil.isNotBlank(optionTypeSql)) {
            try {
                List<Map<String, Object>> mapList = DbContext.use(AnnoConstants.DEFAULT_DATASOURCE_NAME).sql(optionTypeSql).getDataList().getMapList();
                for (Map<String, Object> map : mapList) {
                    mapping.put(map.get("value").toString(), map.get("label"));
                }
            } catch (SQLException e) {
                throw new BizException(e);
            }
        } else if (!optionAnnoClass.getAnnoClass().equals(Object.class)) {
            AnEntity anEntity = AnnoBeanUtils.metadataManager().getEntity(optionAnnoClass.getAnnoClass());
            List<?> dataList = AnnoBeanUtils.getBean(DbServiceWithProxy.class).list(
                anEntity.getClazz(),
                new ArrayList<>()
            );
            for (Object data : dataList) {
                mapping.put(
                    ReflectUtil.getFieldValue(data, optionAnnoClass.getIdKey()),
                    ReflectUtil.getFieldValue(data, optionAnnoClass.getLabelKey())
                );
            }
        } else if (CollUtil.isNotEmpty(anField.getOptionDatas())) {
            for (AnField.OptionData optionData : anField.getOptionDatas()) {
                mapping.put(optionData.getValue(), optionData.getLabel());
            }
        }
        return mapping;
    }

    public static List<Options.Option> getOptions(AnField anField) {
        List<Options.Option> optionItemList = new ArrayList<>();
        Map<Object, Object> optionData = getOptionData(anField);
        for (Map.Entry<Object, Object> entry : optionData.entrySet()) {
            Options.Option option = new Options.Option();
            option.setLabel(StrUtil.toString(entry.getValue()));
            option.setValue(StrUtil.toString(entry.getKey()));
            optionItemList.add(option);
        }
        return optionItemList;
    }


    public static Map<String, Object> createM2mJoinQueryMap(AnColumnButton anColumnButton, boolean m2m) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("joinValue", "${" + anColumnButton.getM2mJoinThisClazzField() + "}");
        queryMap.put("joinCmd", Base64.encodeStr(anColumnButton.getM2mJoinSql().getBytes(), false, true));
        queryMap.put("mediumThisField", anColumnButton.getM2mMediumOtherField());
        queryMap.put("mediumOtherField", anColumnButton.getM2mMediumThisField());
        queryMap.put("mediumTableClass", anColumnButton.getM2mMediumTableClass().getSimpleName());
        queryMap.put("joinThisClazzField", anColumnButton.getM2mJoinThisClazzField());
        queryMap.put("isM2m", m2m);
        return queryMap;
    }

    public static Tabs.Tab createTabForColumnButton(AnColumnButton anColumnButton) {
        Tabs.Tab tab = new Tabs.Tab();
        tab.setTitle(anColumnButton.getName());
        if (anColumnButton.isO2mEnable()) {
            IFrame iFrame = new IFrame();
            iFrame.setHeight(anColumnButton.getO2mWindowHeight());
            iFrame.setSrc("/index.html#/amisSingle/index/" + anColumnButton.getO2mJoinMainClazz().getSimpleName() + "?" + anColumnButton.getO2mJoinOtherField() + "=${" + anColumnButton.getO2mJoinThisField() + "}");
            tab.setBody(List.of(iFrame));
        } else if (anColumnButton.isM2mEnable()) {
            Map<String, Object> queryMap = AmisCommonUtil.createM2mJoinQueryMap(anColumnButton,true);
            IFrame iFrame = new IFrame();
            iFrame.setHeight(anColumnButton.getM2mWindowHeight());
            iFrame.setSrc("/index.html#/amisSingle/index/" + anColumnButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
            tab.setBody(List.of(iFrame));
        }
        return tab;
    }

    public static boolean isPermissionGranted(PermissionProxy permissionProxy,AnColumnButton anColumnButton, AnEntity anEntity) {
        try {
            permissionProxy.checkPermission(anEntity, anColumnButton.getPermissionCode());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
