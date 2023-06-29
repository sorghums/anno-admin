package site.sorghum.anno.filter;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import site.sorghum.anno.util.JSONUtil;
import site.sorghum.anno.util.ThrowableLogUtil;

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
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        String extraData = null;
        HashMap<String,Object> bdMap;
        if (ctx.body().startsWith("{")) {
            bdMap = JSONUtil.parseObject(ctx.body(),HashMap.class);
            if (bdMap.containsKey("_extraData")) {
                extraData = bdMap.get("_extraData").toString();
            }
        } else if (StrUtil.isBlank(ctx.body())) {
            bdMap = new HashMap<>();
        }else {
            bdMap = null;
        }
        if (StrUtil.isNotBlank(ctx.param("_extraData"))) {
            extraData = ctx.param("_extraData");
        }
        if (bdMap != null && StrUtil.isNotBlank(extraData)) {
            try {
                HashMap<String,Object> param = JSONUtil.parseObject(extraData,HashMap.class);
                param.forEach(
                        (k, v) -> {
                            if (v != null) {
                                ctx.paramSet(k, v.toString());
                                bdMap.put(k, v);
                            }
                        }
                );
                ctx.bodyNew(JSONUtil.toJSONString(bdMap));
            } catch (Exception e) {
                ThrowableLogUtil.error(e);
            }
        }

        chain.doFilter(ctx);
    }
}