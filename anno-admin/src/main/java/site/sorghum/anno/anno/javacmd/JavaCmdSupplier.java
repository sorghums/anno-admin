package site.sorghum.anno.anno.javacmd;

import site.sorghum.anno._common.entity.CommenParam;

public interface JavaCmdSupplier {

    /**
     * 运行
     * 返回体：JS运行信息
     *
     * @param param 参数
     * @return {@link String}
     */
    String run(CommenParam param);
}
