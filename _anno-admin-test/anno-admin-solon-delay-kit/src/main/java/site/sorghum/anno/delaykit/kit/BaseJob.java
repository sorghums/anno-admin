package site.sorghum.anno.delaykit.kit;

/**
 * 基础工作
 *
 * @author Sorghum
 * @since 2023/07/24
 */
public interface BaseJob {

    public void run(JobEntity jobEntity) throws Exception;

}
