package site.sorghum.anno.anno.javacmd;

import site.sorghum.anno._common.entity.CommonParam;

public interface JavaCmdSupplier {

    /**
     * 运行
     * 返回体：JS运行信息
     *
     * @param param 参数
     * @return {@link String}
     */
    String run(CommonParam param);

    /**
     * 返回默认成功消息的JS调用字符串
     *
     * @param msg 消息内容
     * @return 返回一个格式化的JS调用字符串，例如：js://createMessage.success('成功')
     */
    default String defaultMsgSuccess(String msg) {
        return "js://createMessage.success('%s')".formatted(msg);
    }

    /**
     * 返回默认失败消息的JS调用字符串
     *
     * @param msg 消息内容
     * @return 返回一个格式化的JS调用字符串，例如：js://createMessage.error('失败')
     */
    default String defaultMsgFail(String msg) {
        return "js://createMessage.error('%s')".formatted(msg);
    }

    /**
     * 将给定的URL转换为iframe URL格式。
     *
     * @param url 要转换的原始URL
     * @return 转换后的iframe URL，格式为 "iframe://%s"，其中 %s 会被替换为传入的url参数
     */
    default String iframeUrl(String url) {
        return "iframe://%s".formatted(url);
    }

    /**
     * 将给定的文档内容转换为iframeDoc格式的URL。
     *
     * @param doc 要转换的文档内容
     * @return 转换后的iframeDoc URL，格式为 "iframeDoc://%s"，其中 %s 会被替换为传入的doc参数
     */
    default String iframeDoc(String doc) {
        return "iframeDoc://%s".formatted(doc);
    }
}
