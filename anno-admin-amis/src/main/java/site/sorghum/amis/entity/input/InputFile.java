package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Api;

/**
 * 输入文件
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputFile extends FormItem{
    {
        setType("input-file");
    }

    /**
     * 上传文件接口
     */
    Api receiver;

    /**
     * 默认只支持纯文本，要支持其他类型，请配置此属性为文件后缀.xxx
     */
    String accept = "text/plain";

    /**
     * 将文件以base64的形式，赋值给当前组件
     */
    Boolean asBase64 = false;

    /**
     * 将文件以二进制的形式，赋值给当前组件
     */
    Boolean asBlob = false;

    /**
     * 默认没有限制，当设置后，文件大小大于此值将不允许上传。单位为B
     */
    Integer maxSize;

    /**
     * 默认没有限制，当设置后，一次只允许上传指定数量文件。
     */
    Integer maxLength;

    /**
     * 是否多选。
     */
    Boolean multiple = false;

    /**
     * 是否为拖拽上传
     */
    Boolean drag = false;

    /**
     * 拼接值
     */
    Boolean joinValues = true;

    /**
     * 提取值
     */
    Boolean extractValue = false;

    /**
     * 拼接符
     */
    String delimiter = ",";

    /**
     * 否选择完就自动开始上传
     */
    Boolean autoUpload = true;

    /**
     * 隐藏上传按钮
     */
    Boolean hideUploadButton = false;

    /**
     * 上传状态文案
     */
    StateText stateTextMap = new StateText();

    /**
     * 如果你不想自己存储，则可以忽略此属性。
     */
    String fileField = "file";

    /**
     * 接口返回哪个字段用来标识文件名
     */
    String nameField = "name";

    /**
     * 文件的值用那个字段来标识。
     */
    String valueField = "url";

    /**
     * 文件下载地址的字段名。
     */
    String urlField = "url";

    /**
     * 上传按钮的文字
     */
    String btnLabel;

    /**
     * 默认显示文件路径的时候会支持直接下载，可以支持加前缀如：http://xx.dom/filename= ，如果不希望这样，可以把当前配置项设置为 false。
     */
    Boolean downloadUrl = false;

/**
     * 默认显示文件路径的时候会支持直接下载，可以支持加前缀如：http://xx.dom/filename= ，如果不希望这样，可以把当前配置项设置为 false。
     */
    String useChunk = "auto";

    /**
     * 分块大小
     */
    Integer chunkSize = 5 * 1024 * 1024;

    /**
     * startChunkApi
     */
    Api startChunkApi;

    /**
     * chunkApi
     */
    Api chunkApi;

    /**
     * finishChunkApi
     */
    Api finishChunkApi;

    /**
     * 分块上传时并行个数
     */
    Integer concurrency;

    /**
     * 文档内容
     */
    String documentation;

    /**
     * 文档链接
     */
    String documentLink;

    /**
     * 初表单反显时是否执行
     */
    Boolean initAutoFill = true;

    @Data
    public static class StateText{
        String init = "";
        String pending = "等待上传";
        String uploading = "上传中";
        String error = "上传出错";
        String uploaded = "已上传";
        String ready = "";
    }
}
