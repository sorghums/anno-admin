package site.sorghum.anno.anno.annotation.clazz;

import site.sorghum.anno.anno.annotation.field.AnnoChartField;
import site.sorghum.anno.anno.annotation.field.AnnoChartFieldImpl;
import site.sorghum.anno.anno.form.BaseForm;
import site.sorghum.anno.anno.form.DefaultBaseForm;

import java.lang.annotation.Annotation;

/**
 * Anno 图表注解实现类
 *
 * @author Qianjiawei
 * @since 2024/01/25
 */
public class AnnoChartImpl implements AnnoChart {
    /**
     * 是否启用
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
        return this.enable;
    }

    @Override
    public int[] layout() {
        return this.layout;
    }

    @Override
    public AnnoChartField[] chartFields() {
        return this.chartFields;
    }

    @Override
    public Class<? extends BaseForm> searchForm() {
        return this.searchForm;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoChart.class;
    }
}