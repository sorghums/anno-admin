package site.sorghum.anno.anno.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnnoPage<T> {
    /**
     * 是否是分页结果
     */
    boolean isPage;

    /**
     * 数据
     */
    List<T> list;

    /**
     * 总数
     */
    long total;

    /**
     * 页大小
     */
    int size;

    /**
     * 页码
     */
    long page;

}
