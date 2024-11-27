package site.sorghum.anno.file;

import cn.hutool.core.io.IoUtil;
import lombok.Data;

import java.io.InputStream;

/**
 * An文件
 * @author Administrator
 */
@Data
public class FileInfo {
    /**
     * 文件名
     */
    String fileName;

    /**
     * 文件路径
     */
    String originalPath = "";

    /**
     * 文件数据
     */
    byte[] bytes;

    /**
     * 文件网络url
     */
    String fileUrl;

    /**
     * 文件流
     */
    InputStream inputStream;

    /**
     * 读写权限
     */
    String acl;

    /**
     * 获取文件数据
     */
    public byte[] getBytes() {
        if (bytes == null) {
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
               '}';
    }
}
