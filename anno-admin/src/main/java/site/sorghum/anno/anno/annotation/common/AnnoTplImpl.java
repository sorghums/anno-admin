package site.sorghum.anno.anno.annotation.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.tpl.BaseTplRender;

import java.lang.annotation.*;

/**
 * 外置页面
 *
 * @author Sorghum
 * @since 2023/09/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoTplImpl implements AnnoTpl {
    /**
     * 解析的Action类
     */
    Class<? extends BaseTplRender> tplClazz = BaseTplRender.class;

    /**
     * 弹出窗口宽度
     */
    String windowWidth = WINDOW_WIDTH;

    /**
     * 弹出窗口高度
     */
    String windowHeight = WINDOW_HEIGHT;

    /**
     * 是否启用
     */
    boolean enable = true;

    @Override
    public Class<? extends BaseTplRender> tplClazz() {
        return this.tplClazz;
    }

    @Override
    public String windowWidth() {
        return this.windowWidth;
    }

    @Override
    public String windowHeight() {
        return this.windowHeight;
    }

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
