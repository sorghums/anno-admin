package site.sorghum.anno.anno.annotation.field.type;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * Anno 图像类型
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
public class AnnoFileTypeImpl implements AnnoFileType {
    /**
     * 文件类型
     */
    private String fileType = "*";

    /**
     * 文件最大计数
     */
    private int fileMaxCount = 1;

    /**
     * 文件最大大小 单位Mb
     */
    private int fileMaxSize = 5;

    @Override
    public String fileType() {
        return this.fileType;
    }

    @Override
    public int fileMaxCount() {
        return this.fileMaxCount;
    }

    @Override
    public int fileMaxSize() {
        return this.fileMaxSize;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoFileType.class;
    }
}