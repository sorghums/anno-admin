package tech.powerjob.server.solon.persistence.storage.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.noear.solon.core.AopContext;
import tech.powerjob.server.solon.extension.dfs.DownloadRequest;
import tech.powerjob.server.solon.extension.dfs.FileLocation;
import tech.powerjob.server.solon.extension.dfs.FileMeta;
import tech.powerjob.server.solon.extension.dfs.StoreRequest;
import tech.powerjob.server.solon.persistence.storage.AbstractDFsService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Alibaba OSS support
 * <a href="https://www.aliyun.com/product/oss">海量、安全、低成本、高可靠的云存储服务</a>
 * 配置项：
 * oms.storage.dfs.alioss.endpoint
 * oms.storage.dfs.alioss.bucket
 * oms.storage.dfs.alioss.credential_type
 * oms.storage.dfs.alioss.ak
 * oms.storage.dfs.alioss.sk
 * oms.storage.dfs.alioss.token
 *
 * @author tjq
 * @since 2023/7/30
 */
@Slf4j
public class AliOssService extends AbstractDFsService {

    private static final String TYPE_ALI_OSS = "alioss";

    private static final String KEY_ENDPOINT = "endpoint";
    private static final String KEY_BUCKET = "bucket";
    private static final String KEY_CREDENTIAL_TYPE = "credential_type";
    private static final String KEY_AK = "ak";
    private static final String KEY_SK = "sk";
    private static final String KEY_TOKEN = "token";

    private OSS oss;
    private String bucket;

    private static final int DOWNLOAD_PART_SIZE = 10240;

    private static final String NO_SUCH_KEY = "NoSuchKey";

    @Override
    public void store(StoreRequest storeRequest) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, parseFileName(storeRequest.getFileLocation()), storeRequest.getLocalFile(), objectMetadata);
        oss.putObject(putObjectRequest);
    }

    @Override
    public void download(DownloadRequest downloadRequest) throws IOException {

        FileLocation dfl = downloadRequest.getFileLocation();
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucket, parseFileName(dfl), downloadRequest.getTarget().getAbsolutePath(), DOWNLOAD_PART_SIZE);
        try {
            FileUtils.forceMkdirParent(downloadRequest.getTarget());
            oss.downloadFile(downloadFileRequest);
        } catch (Throwable t) {
            ExceptionUtils.rethrow(t);
        }
    }

    @Override
    public Optional<FileMeta> fetchFileMeta(FileLocation fileLocation) throws IOException {
        try {
            ObjectMetadata objectMetadata = oss.getObjectMetadata(bucket, parseFileName(fileLocation));
            return Optional.ofNullable(objectMetadata).map(ossM -> {

                Map<String, Object> metaInfo = Maps.newHashMap();
                metaInfo.putAll(ossM.getRawMetadata());
                if (ossM.getUserMetadata() != null) {
                    metaInfo.putAll(ossM.getUserMetadata());
                }

                return new FileMeta()
                    .setLastModifiedTime(ossM.getLastModified())
                    .setLength(ossM.getContentLength())
                    .setMetaInfo(metaInfo);
            });
        } catch (OSSException oe) {
            String errorCode = oe.getErrorCode();
            if (NO_SUCH_KEY.equalsIgnoreCase(errorCode)) {
                return Optional.empty();
            }
            ExceptionUtils.rethrow(oe);
        }
        return Optional.empty();
    }

    private static String parseFileName(FileLocation fileLocation) {
        return String.format("%s/%s", fileLocation.getBucket(), fileLocation.getName());
    }

    void initOssClient(String endpoint, String bucket, String mode, String ak, String sk, String token) throws Exception {

        log.info("[AliOssService] init OSS by config: endpoint={},bucket={},credentialType={},ak={},sk={},token={}", endpoint, bucket, mode, ak, sk, token);

        if (StringUtils.isEmpty(bucket)) {
            throw new IllegalArgumentException("'oms.storage.dfs.alioss.bucket' can't be empty, please creat a bucket in aliyun oss console then config it to powerjob");
        }

        this.bucket = bucket;

        CredentialsProvider credentialsProvider;
        CredentialType credentialType = CredentialType.parse(mode);
        switch (credentialType) {
            case PWD:
                credentialsProvider = new DefaultCredentialProvider(ak, sk, token);
                break;
            case SYSTEM_PROPERTY:
                credentialsProvider = CredentialsProviderFactory.newSystemPropertiesCredentialsProvider();
                break;
            default:
                credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        }

        this.oss = new OSSClientBuilder().build(endpoint, credentialsProvider);
        log.info("[AliOssService] initialize successfully, THIS_WILL_BE_THE_STORAGE_LAYER.");
    }

    @Override
    public void cleanExpiredFiles(String bucket, int days) {
        /*
        阿里云 OSS 自带生命周期管理，请参考文档进行配置，代码层面不进行实现（浪费服务器资源）https://help.aliyun.com/zh/oss/user-guide/overview-54
        阿里云 OSS 自带生命周期管理，请参考文档进行配置，代码层面不进行实现（浪费服务器资源）https://help.aliyun.com/zh/oss/user-guide/overview-54
        阿里云 OSS 自带生命周期管理，请参考文档进行配置，代码层面不进行实现（浪费服务器资源）https://help.aliyun.com/zh/oss/user-guide/overview-54
         */
    }

    @Override
    protected void init(AopContext context) {

        String endpoint = fetchProperty(context, TYPE_ALI_OSS, KEY_ENDPOINT);
        String bkt = fetchProperty(context, TYPE_ALI_OSS, KEY_BUCKET);
        String ct = fetchProperty(context, TYPE_ALI_OSS, KEY_CREDENTIAL_TYPE);
        String ak = fetchProperty(context, TYPE_ALI_OSS, KEY_AK);
        String sk = fetchProperty(context, TYPE_ALI_OSS, KEY_SK);
        String token = fetchProperty(context, TYPE_ALI_OSS, KEY_TOKEN);

        try {
            initOssClient(endpoint, bkt, ct, ak, sk, token);
        } catch (Exception e) {
            ExceptionUtils.rethrow(e);
        }
    }

    @Getter
    @AllArgsConstructor
    enum CredentialType {
        /**
         * 从环境读取
         */
        ENV("env"),
        /**
         * 系统配置
         */
        SYSTEM_PROPERTY("sys"),
        /**
         * 从账号密码读取
         */
        PWD("pwd");

        private final String code;

        /**
         * parse credential type
         *
         * @param mode oms.storage.dfs.alioss.credential_type
         * @return CredentialType
         */
        public static CredentialType parse(String mode) {

            for (CredentialType credentialType : values()) {
                if (StringUtils.equalsIgnoreCase(credentialType.code, mode)) {
                    return credentialType;
                }
            }

            return PWD;
        }

    }

}
