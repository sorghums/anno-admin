package site.sorghum.anno.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Anno数据类型
 *
 * @author sorghum
 * @since 2023/05/27
 */
@AllArgsConstructor
@Getter
public enum AnnoDataType {
    STRING( "字符串"),
    LINK( "链接"),
    FILE("文件"),
    IMAGE( "图片"),
    NUMBER("数字"),
    DATE("日期"),
    DATETIME("日期时间"),
    OPTIONS("下拉框"),
    RADIO("单选矿"),
    PICKER("下拉框[弹出]"),
    TREE("树形下拉框"),
    CLASS_OPTIONS("类下拉框[高级]"),
    RICH_TEXT("富文本"),
    CODE_EDITOR("代码编辑器"),
    TEXT_AREA("长文本"),
    MARK_DOWN("MarkDown文本"),
    COLOR("颜色"),
    QR_CODE("二维码"),
    AVATAR("头像"),
    ICON("图标");

    /**
     * 代码
     */
    private final String name;


    /**
     * 根据枚举名称获取枚举值
     *
     * @param INSTANCE_NAME 枚举名称
     * @return 对应的枚举值
     * @throws IllegalArgumentException 如果未找到对应枚举值，则抛出此异常
     */
    public static AnnoDataType getByName(String INSTANCE_NAME) {
        AnnoDataType[] annoDataTypes = AnnoDataType.values();
        for (AnnoDataType INSTANCE : annoDataTypes) {
            if(INSTANCE_NAME.equals(INSTANCE.toString())){
                return INSTANCE;
            }
        }
        throw new IllegalArgumentException("未找到对应枚举");
    }

}
