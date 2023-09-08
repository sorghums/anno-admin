package site.sorghum.anno.amis.util;

import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.input.FormItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AMIS常用方法集合
 * @author Sorghum
 * @since 2023/09/08
 */
public class AmisCommonUtil {
    public static List<AmisBase> formItemToGroup(List<AmisBase> formItems, Integer columnCnt) {
        String[] ignoreTypes = {
            "editor","input-rich-text","input-image"
        };
        // 加入传入的忽略类型
        Map<Integer,List<AmisBase>> indexMap = new HashMap<>();
        List<AmisBase> rst = new ArrayList<>();
        int idx = 0;
        for (AmisBase formItem : formItems) {
            if (Boolean.TRUE.equals(formItem.getHidden())){
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
                    if (!nowIdxFormItems.isEmpty()){
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
                if (!nowIdxFormItems.isEmpty()){
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


}
