package site.sorghum.anno.endpoint;

import site.sorghum.anno._common.entity.CommonParam;

/**
 * 基端点
 *
 * @author Sorghum
 * @since 2024/07/26
 */
public interface BaseEndPoint {

    /**
     * 处理通用的参数并返回处理结果。
     *
     * @param commonParam 通用参数对象，包含了所有处理所需的参数。
     * @return 处理后的结果对象，具体类型取决于实际处理逻辑。
     */
    Object process(CommonParam commonParam);

}
