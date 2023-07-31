package site.sorghum.anno.i18n;

/**
 * i18n服务
 *
 * @author Sorghum
 * @since 2023/07/31
 */
public interface I18nService {
    /**
     * 获取翻译后内容
     *
     * @param key 关键
     * @return {@link String}
     */
    String getMessage(String key);
}
