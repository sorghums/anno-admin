package site.sorghum.anno.endpoint;

import lombok.Data;
import site.sorghum.anno._common.entity.CommonParam;


@Data
public class EndPointBody {
    /**
     * endPoint名称
     */
    String endPointName;

    /**
     * 参数
     */
    CommonParam commonParam;
}
