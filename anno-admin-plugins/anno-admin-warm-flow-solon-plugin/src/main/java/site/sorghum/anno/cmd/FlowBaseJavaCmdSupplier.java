package site.sorghum.anno.cmd;

import org.dromara.warm.flow.core.exception.FlowException;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

import java.util.function.Supplier;

public interface FlowBaseJavaCmdSupplier extends JavaCmdSupplier {

    /**
     * 使用给定的Supplier获取字符串。
     *
     * @param supplier 一个Supplier<String>类型的参数，用于提供字符串
     * @return 从supplier中获取的字符串
     * @throws BizException 如果在获取字符串时发生FlowException，则抛出BizException异常
     */
    default String parcel(Supplier<String> supplier){
        try {
            return supplier.get();
        }catch (FlowException e){
            throw new BizException(e.getMessage(),e);
        }
    };
}
