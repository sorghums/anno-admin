package site.sorghum.amis;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AmisTemplate {
    /**
     * 前后分隔符
     */
    private static final Character BASE_CHAR = '"';

    /**
     * amis模板原始内容
     */
    private String originalContent;
    /**
     * amis模板中的key&value
     */
    private List<AmisTemplateModel> amisTemplateModels;

    /**
     * 获取amis模板中的key
     *
     * @param amisTemplateKey amis模板中的key
     * @return {@link String}
     */
    private String getAmisTemplateKey(String amisTemplateKey) {
        if (amisTemplateKey.startsWith("\"") && amisTemplateKey.endsWith("\"")) {
            return amisTemplateKey;
        }
        if (!amisTemplateKey.startsWith("{{{") && !amisTemplateKey.endsWith("}}}")) {
            amisTemplateKey = "{{{" + amisTemplateKey + "}}}";
        }
        return BASE_CHAR + amisTemplateKey + BASE_CHAR;
    }

    /**
     * 填充值
     *
     * @param amisTemplateKey amis模板中的key
     * @param value           amis模板中的value
     */
    private void fillValue(String amisTemplateKey, Object value) {
        CharSequence charSequence = null;
        if (value instanceof CharSequence sequence) {
            charSequence = sequence;
        } else {
            if (value == null) {
                charSequence = "";
            } else {
                charSequence = JSON.toJSONString(value);
            }
        }
        this.originalContent = this.originalContent.replace(getAmisTemplateKey(amisTemplateKey), charSequence);
    }

    public String getFilledContent() {
        for (AmisTemplateModel amisTemplateModel : amisTemplateModels) {
            fillValue(amisTemplateModel.getKey(), amisTemplateModel.getValue());
        }
        return this.originalContent;
    }


}
