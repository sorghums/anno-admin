package site.sorghum.anno.modular.message.entity.response;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import site.sorghum.anno.modular.message.entity.model.AnnoMessage;
import lombok.Data;

import java.util.List;

/**
 * Anno菜单响应
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Data
public class AnnoMessageResponse {
    /**
     * 消息ID 1 通知 2 消息 3 待办
     */
    @JSONField(name = "id")
    String id;
    /**
     * 消息标题 1 通知 2 消息 3 待办
     */
    @JSONField(name = "title")
    String title;
    /**
     * 子节点
     */
    @JSONField(name = "children")
    List<AnnoMessage> children;

    public static List<AnnoMessageResponse> DEMO() {
        String demoMessage = "[\n" +
                "                    {\n" +
                "                        \"id\": 1,\n" +
                "                        \"title\": \"通知\",\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"id\": 11,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png\",\n" +
                "                                \"title\": \"你收到了 14 份新周报\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 12,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png\",\n" +
                "                                \"title\": \"曲妮妮 已通过第三轮面试\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 11,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png\",\n" +
                "                                \"title\": \"可以区分多种通知类型\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 12,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png\",\n" +
                "                                \"title\": \"左侧图标用于区分不同的类型\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 11,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png\",\n" +
                "                                \"title\": \"内容不要超过两行字\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 2,\n" +
                "                        \"title\": \"消息\",\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"id\": 11,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png\",\n" +
                "                                \"title\": \"你收到了 14 份新周报\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 12,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png\",\n" +
                "                                \"title\": \"曲妮妮 已通过第三轮面试\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 11,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png\",\n" +
                "                                \"title\": \"可以区分多种通知类型\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 12,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png\",\n" +
                "                                \"title\": \"左侧图标用于区分不同的类型\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"id\": 11,\n" +
                "                                \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png\",\n" +
                "                                \"title\": \"内容不要超过两行字\",\n" +
                "                                \"context\": \"这是消息内容。\",\n" +
                "                                \"form\": \"就眠仪式\",\n" +
                "                                \"time\": \"刚刚\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 3,\n" +
                "                        \"title\": \"代办\",\n" +
                "                        \"children\": []\n" +
                "                    }\n" +
                "                ]";
        return JSON.parseArray(demoMessage, AnnoMessageResponse.class);
    }


}
