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

}
