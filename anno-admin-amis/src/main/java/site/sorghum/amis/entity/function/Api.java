package site.sorghum.amis.entity.function;

import lombok.Data;

import java.util.Map;

/**
 * api
 *
 * @author sorghum
 * @date 2023/06/30
 */
@Data
public class Api {

    /**
     * 支持：get、post、put、delete
     */
    String method;

    /**
     * 接口地址
     */
    String url;

    /**
     * 请求数据
     * 支持数据映射
     */
    Map<String, Object> data;


    /**
     * 默认为 json 可以配置成 form 或者 form-data。
     * 当 data 中包含文件时，自动会采用 form-data（multipart/form-data） 格式。
     * 当配置为 form 时为 application/x-www-form-urlencoded 格式。
     */
    String contentType;

    /**
     * 当 dataType 为 form 或者 form-data 的时候有用。具体参数请参考这里，
     * 默认设置为: { arrayFormat: 'indices', encodeValuesOnly: true }
     */
    QsOptions qsOptions;

    /**
     * 请求头
     */
    Map<String, Object> headers;

    /**
     * 请求条件
     */
    String sendOn;

    /**
     * 接口缓存时间
     */
    Integer cache;

    /**
     * 发送适配器
     */
    String requestAdaptor;

    /**
     * 接收适配器
     */
    String adaptor;

    /**
     * 替换当前数据
     * 返回的数据是否替换掉当前的数据，默认为 false，即：追加，设置成 true 就是完全替换。
     */
    Boolean replaceData = false;

    /**
     * 如果是下载需要设置为 'blob'
     */
    String responseType;

    /**
     * 配置是否需要自动刷新接口。
     */
    Boolean autoRefresh;

    /**
     * 对返回结果做个映射
     */
    Map<String, Object> responseData;

    /**
     * 配置跟踪变量表达式
     * trackExpression，显式的配置需要跟踪的变量
     */
    String trackExpression;

    /**
     * 配置接口请求的提示信息，messages.success 表示请求成功提示信息、messages.failed 表示请求失败提示信息，2.4.1 及以上版本
     */
    ApiMessage messages;

    @Data
    public static class ApiMessage {
        /**
         * 请求成功提示信息
         */
        String success;
        /**
         * 请求失败提示信息
         */
        String failed;
    }

    @Data
    public static class QsOptions {
        /**
         * 用来设置数组格式，支持 indices、brackets、repeat、comma、separator、bracket、none
         */
        String arrayFormat = "indices";
        /**
         * 用来设置是否只编码值，为 true 时，只会编码值，不会编码键名。
         */
        Boolean encodeValuesOnly = true;
    }

}
