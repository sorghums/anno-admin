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
    default String defaultMsgSuccess(String msg){
        return "js://createMessage.success('%s')".formatted(msg);
    }

    /**
     * 返回默认失败消息的JS调用字符串
     *
     * @param msg 消息内容
     * @return 返回一个格式化的JS调用字符串，例如：js://createMessage.error('失败')
     */
    default String defaultMsgFail(String msg){
        return "js://createMessage.error('%s')".formatted(msg);
    }
}
