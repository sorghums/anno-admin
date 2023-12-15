package tech.powerjob.server.solon.core.service;

import tech.powerjob.common.PowerQuery;
import tech.powerjob.common.request.http.SaveJobInfoRequest;
import tech.powerjob.common.response.JobInfoDTO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

import java.util.List;

/**
 * JobService
 *
 * @author tjq
 * @since 2023/3/4
 */
public interface JobService {

    String saveJob(SaveJobInfoRequest request);

    JobInfoDO copyJob(String jobId);

    JobInfoDTO fetchJob(String jobId);

    List<JobInfoDTO> fetchAllJob(String appId);

    List<JobInfoDTO> queryJob(PowerQuery powerQuery);

    String runJob(String appId, String jobId, String instanceParams, Long delay);

    void deleteJob(String jobId);

    void disableJob(String jobId);

    void enableJob(String jobId);

    SaveJobInfoRequest exportJob(String jobId);
}
