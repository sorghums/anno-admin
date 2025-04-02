package site.sorghum.anno.file;

import cn.hutool.core.io.IoUtil;
import lombok.Data;

import java.io.InputStream;

/**
 * 文件信息封装类
 * 包含文件元数据和内容数据
 */
@Data
public class FileInfo {
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原始文件路径
     */
    private String originalPath = "";

    /**
     * 文件字节数据
     */
    private byte[] bytes;

    /**
     * 文件访问URL
     */
    private String fileUrl;

    /**
     * 文件输入流
     */
    private transient InputStream inputStream;

    /**
     * 文件访问控制权限
     */
    private String acl;

    /**
     * 获取文件字节数据
     * 如果bytes为空，则从inputStream读取
     *
     * @return 文件字节数据
     */
    public byte[] getBytes() {
        if (bytes == null && inputStream != null) {
            bytes = IoUtil.readBytes(inputStream);
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
            "fileName='" + fileName + '\'' +
            ", originalPath='" + originalPath + '\'' +
            ", fileUrl='" + fileUrl + '\'' +
            ", acl='" + acl + '\'' +
            ", bytesSize=" + (bytes != null ? bytes.length : 0) +
            '}';
    }
}