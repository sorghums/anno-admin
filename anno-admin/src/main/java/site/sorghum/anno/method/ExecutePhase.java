package site.sorghum.anno.method;

/**
 * 方法单元的执行阶段，执行顺序 before -> execute -> after
 *
 * @author songyinyin
 * @since 2024/1/29 20:50
 */
public enum ExecutePhase {
    /**
     * 前置处理阶段（参数校验等）
     */
    BEFORE,
    /**
     * 执行阶段（业务逻辑执行）
     */
    EXECUTE,
    /**
     * 后置处理阶段
     */
    AFTER
}
