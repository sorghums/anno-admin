package site.sorghum.anno.anno.annotation.field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.annotation.clazz.AnnoChart;
import site.sorghum.anno.anno.annotation.field.AnnoChartField;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;

import java.lang.annotation.Annotation;

/**
 * anno图表
 *
 * @author Qianjiawei
 * @since 2024/07/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoChartImpl implements AnnoChart {
    /**
     * 启用
     */
    private boolean enable = true;

    /**
     * 布局
     */
    private int[] layout = {1, 2, 3};

    /**
     * 图表字段
     */
    private AnnoChartFieldImpl[] chartFields = {};

    /**
     * 查询表单提供类
     */
    private Class<? extends BaseForm> searchForm = DefaultBaseForm.class;

    @Override
    public boolean enable() {
        return enable;
    }

    @Override
    public int[] layout() {
        return layout;
    }

    @Override
    public AnnoChartField[] chartFields() {
        return chartFields;
    }

    @Override
    public Class<? extends BaseForm> searchForm() {
        return searchForm;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoChart.class;
    }

    public AnnoChartFieldImpl[] getChartFields() {
        if (chartFields == null) {
            return new AnnoChartFieldImpl[0];
        }
        return chartFields;
    }
}