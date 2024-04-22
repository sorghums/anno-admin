package site.sorghum.anno.anno.javacmd.supplier;

import java.util.Map;

public interface JavaCmdSupplier {

    /**
     * 运行
     * 返回体：JS运行信息
     *
     * @param param 参数
     * @return {@link String}
     */
    String run(Map<String, Object> param);
}
