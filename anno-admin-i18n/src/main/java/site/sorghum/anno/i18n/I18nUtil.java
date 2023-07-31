package site.sorghum.anno.i18n;


/**
 * i18n工具类
 *
 * @author Sorghum
 * @since 2023/07/31
 */
public class I18nUtil {
    private static I18nService i18nService;

    /**
     * 翻译
     */
    public static String getMessage(String key){
        return i18nService.getMessage(key);
    }

    public static void setI18nService(I18nService i18nService){
        I18nUtil.i18nService = i18nService;
    }

}
