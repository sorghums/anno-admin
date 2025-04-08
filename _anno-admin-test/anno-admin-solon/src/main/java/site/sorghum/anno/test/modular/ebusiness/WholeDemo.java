package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.*;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.*;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AnnoMain(
    name = "啥都有",
    tableName = "whole_demo",
    annoOrder = {
        @AnnoOrder(orderValue = "id",orderType = "desc")
    },
    annoPermission = @AnnoPermission(baseCode = "whole_demo",baseCodeTranslate = "啥都有"),
    annoLeftTree = @AnnoLeftTree(leftTreeName = "demo",catKey = "id",treeClass = WholeDemo.class),
    annoTree = @AnnoTree(label = "name", parentKey = "pid", key = "id", displayAsTree = false),
    canRemove = true,
    autoMaintainTable = true
)
public class WholeDemo {

    @AnnoField(
        title = "主键",
        tableFieldName = "id",
        fieldSize = 32,
        show = true,
        pkField = true,
        edit = @AnnoEdit(
            editEnable = false,
            addEnable = false
        ),
        insertWhenNullSet = SnowIdSupplier.class,
        search = @AnnoSearch(
            notNull = false,
            enable = true,
            defaultValue = "",
            defaultValueSupplier = EmptyFieldBaseSupplier.class
        ),
        dataType = AnnoDataType.STRING,
        optionType = @AnnoOptionType(
            value = {
                @AnnoOptionType.OptionData(value = "1", label = "一"),
                @AnnoOptionType.OptionData(value = "2", label = "二")
            }
        ),
        imageType = @AnnoImageType(
            enlargeAble = true,
            width = 50,
            height = 50
        ),
        codeType = @AnnoCodeType(
            mode = "text/x-java"
        ),
        treeType = @AnnoTreeType(
            value = {
                @AnnoTreeType.TreeData(id = "1", label = "一",pid = ""),
                @AnnoTreeType.TreeData(id = "2", label = "二",pid = ""),
                @AnnoTreeType.TreeData(id = "1.1", label = "一.一", pid = "1")
            }
        ),
        fileType = @AnnoFileType(
            fileType = "*",
            fileMaxCount = 1,
            fileMaxSize = 1024
        )
    )
    String id;

    @AnnoField(
        title = "父级ID",
        tableFieldName = "pid",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.TREE,
        search = @AnnoSearch(
            notNull = false,
            enable = true,
            defaultValue = "",
            defaultValueSupplier = EmptyFieldBaseSupplier.class
        ),
        treeType = @AnnoTreeType(
            treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = WholeDemo.class)
        )
    )
    String pid;

    @AnnoField(
        title = "名称",
        tableFieldName = "name",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        search = @AnnoSearch(
            notNull = false,
            enable = true,
            defaultValue = "",
            defaultValueSupplier = EmptyFieldBaseSupplier.class
        ),
        dataType = AnnoDataType.STRING
    )
    String name;

    // 下拉框数据

    @AnnoField(
        title = "下拉框数据一",
        tableFieldName = "option_value_1",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            value = {
                @AnnoOptionType.OptionData(value = "1", label = "一"),
                @AnnoOptionType.OptionData(value = "2", label = "二")
            }
        )
    )
    String optionValue1;


    @AnnoField(
        title = "下拉框数据二",
        tableFieldName = "option_value_2",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = WholeDemo.class)
        )
    )
    String optionValue2;

    @AnnoField(
        title = "下拉框数据三",
        tableFieldName = "option_value_3",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.CLASS_OPTIONS,
        optionType = @AnnoOptionType(
            optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = WholeDemo.class)
        )
    )
    String optionValue3;

    @AnnoField(
        title = "下拉框数据四",
        tableFieldName = "option_value_4",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            supplier = OptionDataSupplier.class
        )
    )
    String optionValue4;

    // 图片
    @AnnoField(
        title = "图片",
        tableFieldName = "demo_image",
        fieldSize = 256,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.IMAGE,
        imageType = @AnnoImageType(
            width = 100,
            height = 100
        )
    )
    String demoImage;

    // 文件
    @AnnoField(
        title = "文件",
        tableFieldName = "demo_file",
        fieldSize = 256,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.FILE,
        fileType = @AnnoFileType(
            fileType = "*",
            fileMaxCount = 1,
            fileMaxSize = 1024
        )
    )
    String demoFile;

    // 数字
    @AnnoField(
        title = "数字",
        tableFieldName = "demo_number",
        fieldSize = 8,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.NUMBER
    )
    Integer demoNumber;

    // 日期
    @AnnoField(
        title = "日期",
        tableFieldName = "demo_date",
        fieldSize = 0,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.DATE
    )
    Date demoDate;

    // 日期时间
    @AnnoField(
        title = "日期时间",
        tableFieldName = "demo_datetime",
        fieldSize = 0,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.DATETIME
    )
    Date demoDatetime;

    // 单选框
    @AnnoField(
        title = "单选框",
        tableFieldName = "demo_radio",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.RADIO,
        optionType = @AnnoOptionType(
            optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = WholeDemo.class)
        )
    )
    String demoRadio;

    // 树形下拉框

    @AnnoField(
        title = "树形下拉框",
        tableFieldName = "demo_tree",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.TREE,
        treeType = @AnnoTreeType(
            treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = WholeDemo.class)
        )
    )
    String demoTree;

    // 富文本
    @AnnoField(
        title = "富文本",
        tableFieldName = "demo_rich_text",
        fieldSize = 65535,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.RICH_TEXT
    )
    String demoRichText;


    // Java编辑器
    @AnnoField(
        title = "Java编辑器",
        tableFieldName = "demo_java_editor",
        fieldSize = 65535,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.CODE_EDITOR,
        codeType = @AnnoCodeType(
            mode = "text/x-java"
        )
    )
    String demoJavaEditor;

    // 长文本
    @AnnoField(
        title = "长文本",
        tableFieldName = "demo_textarea",
        fieldSize = 65535,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.TEXT_AREA
    )
    String demoTextArea;

    // MarkDown文本
    @AnnoField(
        title = "MarkDown文本",
        tableFieldName = "demo_markdown",
        fieldSize = 65535,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.MARK_DOWN
    )
    String demoMarkDown;

    // 颜色
    @AnnoField(
        title = "颜色",
        tableFieldName = "demo_color",
        fieldSize = 32,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.COLOR
    )
    String demoColor;

    // 二维码
    @AnnoField(
        title = "二维码",
        tableFieldName = "demo_qrcode",
        fieldSize = 255,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.QR_CODE
    )
    String demoQrCode;

    // 头像
    @AnnoField(
        title = "头像",
        tableFieldName = "demo_avatar",
        fieldSize = 255,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.AVATAR
    )
    String demoAvatar;

    // 图标
    @AnnoField(
        title = "图标",
        tableFieldName = "demo_icon",
        fieldSize = 255,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.ICON
    )
    String demoIcon;

    // 链接
    @AnnoField(
        title = "链接",
        tableFieldName = "demo_link",
        fieldSize = 255,
        show = true,
        pkField = false,
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.LINK
    )
    String demoLink;

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
