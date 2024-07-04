package site.sorghum.anno.anno.annotation.field;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * Anno编辑
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
public class AnnoEditImpl implements AnnoEdit {
    /**
     * 是否必填
     */
    boolean notNull = false;

    /**
     * 提示信息
     */
    String placeHolder = "";

    /**
     * 编辑跨度
     * 默认为 24 [1-24] 24为整行
     */
    int span = 0;

    /**
     * 新增启用
     */
    boolean addEnable = true;

    /**
     * 编辑启用
     */
    boolean editEnable = true;

    /**
     * 是否可以清空,一般情况下updateById不会设置为空.
     */
    boolean canClear = false;

    /**
     * 展示依赖条件
     */
    ShowByImpl showBy = new ShowByImpl(){{
        this.enable = false;
    }};

    @Override
    public boolean notNull() {
        return this.notNull;
    }

    @Override
    public String placeHolder() {
        return this.placeHolder;
    }

    @Override
    public int span() {
        return this.span;
    }

    @Override
    public boolean addEnable() {
        return this.addEnable;
    }

    @Override
    public boolean editEnable() {
        return this.editEnable;
    }

    @Override
    public boolean canClear() {
        return this.canClear;
    }

    @Override
    public AnnoEdit.ShowBy showBy() {
        return this.showBy;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoEdit.class;
    }

    /**
     * 展示方式注解
     */
    @Data
    public class ShowByImpl implements AnnoEdit.ShowBy {
        /**
         * 是否启用展示
         */
        boolean enable = true;
        /**
         * 展示条件表达式
         * 必须以：annoDataForm.xx 为变量的取值
         */
        String expr = "annoDataForm.id == 0";

        @Override
        public boolean enable() {
            return this.enable;
        }

        @Override
        public String expr() {
            return this.expr;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return AnnoEdit.ShowBy.class;
        }
    }

}
