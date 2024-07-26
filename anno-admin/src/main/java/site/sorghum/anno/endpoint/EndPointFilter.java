package site.sorghum.anno.endpoint;

import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.entity.CommonParam;


@Slf4j
public class EndPointFilter {

    public static Object filter(String beanName, CommonParam commonParam) {
        try {
            BaseEndPoint bean = AnnoBeanUtils.getBean(beanName);
            return bean.process(commonParam);
        } catch (Exception e) {
            log.error("EndPoint执行出错：{}", e.getMessage());
            throw e;
        }
    }
}
