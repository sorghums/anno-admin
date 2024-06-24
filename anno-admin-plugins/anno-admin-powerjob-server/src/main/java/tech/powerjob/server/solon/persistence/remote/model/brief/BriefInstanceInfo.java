package tech.powerjob.server.solon.persistence.remote.model.brief;


import lombok.Data;

/**
 * @author Echo009
 * @since 2022/9/13
 */
@Data
public class BriefInstanceInfo {


    private String appId;

    private String id;
    /**
     * 任务ID
     */
    private String jobId;
    /**
     * 任务所属应用的ID，冗余提高查询效率
     */
    private String instanceId;
    /**
     * 总共执行的次数（用于重试判断）
     */
    private Long runningTimes;

    public BriefInstanceInfo() {
    }

    public BriefInstanceInfo(String appId, String id, String jobId, String instanceId) {
        this.appId = appId;
        this.id = id;
        this.jobId = jobId;
        this.instanceId = instanceId;
    }

    public BriefInstanceInfo(String appId, String id, String jobId, String instanceId, Long runningTimes) {
        this.appId = appId;
        this.id = id;
        this.jobId = jobId;
        this.instanceId = instanceId;
        this.runningTimes = runningTimes;
    }
}
