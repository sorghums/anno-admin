package site.sorghum.anno.filter;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import site.sorghum.anno.util.ThrowableLogUtil;

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
        JSONObject bdMap;
        if (ctx.body().startsWith("{")) {
            bdMap = JSON.parseObject(ctx.body());
            if (bdMap.containsKey("_extraData")) {
                extraData = bdMap.getString("_extraData");
            }
        } else if (StrUtil.isBlank(ctx.body())) {
            bdMap = new JSONObject();
        }else {
            bdMap = null;
        }
        if (StrUtil.isNotBlank(ctx.param("_extraData"))) {
            extraData = ctx.param("_extraData");
        }
        if (bdMap != null && StrUtil.isNotBlank(extraData)) {
            try {
                JSONObject param = JSON.parseObject(extraData);
                param.forEach(
                        (k, v) -> {
                            if (v != null) {
                                ctx.paramSet(k, v.toString());
                                bdMap.put(k, v);
                            }
                        }
                );
                ctx.bodyNew(bdMap.toJSONString());
            } catch (Exception e) {
                ThrowableLogUtil.error(e);
            }
        }

        chain.doFilter(ctx);
    }
}