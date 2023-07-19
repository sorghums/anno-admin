package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.solon.i18n.I18nUtil;

/**
 * 输入时间
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputTime extends FormItem{
    {
        setType("input-time");
    }
    /**
     * 默认值
     */
    String value;

    /**
     * 时间选择器值格式，更多格式类型请参考 moment.js
     */
    String format = "X";

    /**
     * 时间选择器值格式，更多格式类型请参考 moment
     */
    String timeFormat = "HH:mm";

    /**
     * 时间选择器显示格式，即时间戳格式，更多格式类型请参考 moment
     */
    String inputFormat = "HH:mm";

    /**
     * 占位文本
     */
    String placeholder = I18nUtil.getMessage("amis.input-time.placeholder");

    /**
     * 是否可清除
     */
    Boolean clearable = true;

    /**
     * 控制输入范围
     */
    TimeConstraints timeConstraints;


    @Data
    public static class TimeConstraints{
        /**
         * 小时
         */
        TimeConstraintsItem hours;

        /**
         * 分钟
         */
        TimeConstraintsItem minutes;

        /**
         * 秒
         */
        TimeConstraintsItem seconds;

        /**
         * 毫秒
         */
        TimeConstraintsItem milliseconds;

        @Data
        public static class TimeConstraintsItem {
            /**
             * 限制最小时间
             */
            Integer min;

            /**
             * 限制最大时间
             */
            Integer max;

            /**
             * 限制步长
             */
            Integer step;
        }
    }




}
