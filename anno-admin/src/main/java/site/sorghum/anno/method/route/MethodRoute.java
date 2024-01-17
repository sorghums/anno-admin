package site.sorghum.anno.method.route;

import site.sorghum.anno.method.MTContext;

/**
 * @author songyinyin
 * @since 2024/1/17 10:31
 */
public interface MethodRoute {

    /**
     * 路由
     *
     * @param context 上下文
     * @return 路由结果
     */
    String[] route(MTContext context);

}
