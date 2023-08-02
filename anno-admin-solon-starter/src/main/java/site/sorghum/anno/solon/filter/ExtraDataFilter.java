package site.sorghum.anno.solon.filter;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._common.util.ThrowableLogUtil;

import java.util.HashMap;

/**
 * 额外数据重新设置
 *
 * @author Sorghum
 * @since 2023/02/24
 */
@Component
@Slf4j
public class ExtraDataFilter implements Filter {
    private static final String EXTRA_DATA = "_extraData";

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if (ctx.isMultipart()){
            chain.doFilter(ctx);
            return;
        }
        String extraData = null;
        HashMap<String,Object> bdMap;
        if (ctx.body().startsWith("{")) {
            bdMap = JSONUtil.toBean(ctx.body(),HashMap.class);
            if (bdMap.containsKey("_extraData")) {
                extraData = MapUtil.getStr(bdMap,EXTRA_DATA);
            }
        } else if (StrUtil.isBlank(ctx.body())) {
            bdMap = new HashMap<>();
        }else {
            bdMap = null;
        }
        if (StrUtil.isNotBlank(ctx.param(EXTRA_DATA))) {
            extraData = ctx.param(EXTRA_DATA);
        }
        if (bdMap != null && StrUtil.isNotBlank(extraData)) {
            try {
                HashMap<String,Object> param = JSONUtil.toBean(extraData,HashMap.class);
                param.forEach(
                        (k, v) -> {
                            if (ObjUtil.isNotEmpty(v)) {
                                ctx.paramSet(k, v.toString());
                                bdMap.put(k, v);
                            }
                        }
                );
                ctx.bodyNew(JSONUtil.toJsonString(bdMap));
            } catch (Exception e) {
                ThrowableLogUtil.error(e);
            }
        }
        AnnoContextUtil.getContext().setRequestParams(bdMap);
        chain.doFilter(ctx);
    }
}