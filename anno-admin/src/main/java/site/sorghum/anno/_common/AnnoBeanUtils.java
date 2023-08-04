package site.sorghum.anno._common;

import site.sorghum.anno._metadata.MetadataManager;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/30 15:45
 */
public class AnnoBeanUtils {

    private static AnnoBean annoBean;

    public static void setBean(AnnoBean annoBean) {
        AnnoBeanUtils.annoBean = annoBean;
    }

    public static <T> T getBean(String name) {
        return annoBean.getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        return annoBean.getBean(type);
    }

    public static <T> List<T> getBeansOfType(Class<T> type) {
        return annoBean.getBeansOfType(type);
    }

    public static MetadataManager metadataManager() {
        return annoBean.getBean(MetadataManager.class);
    }
}
