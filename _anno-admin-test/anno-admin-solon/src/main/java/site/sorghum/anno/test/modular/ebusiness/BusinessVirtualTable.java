package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;

/**
 * 电商商品虚拟表
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "商品虚拟表",
    virtualTable = true,
    annoTableButton = {
        @AnnoTableButton(name = "跳去百度", jumpUrl = "https://www.baidu.com/?tn=${clazz}&props=${props}"),
        @AnnoTableButton(name = "简单的JS命令", jsCmd = "alert('点击了按钮');"),
    }
)
public class BusinessVirtualTable extends BaseMetaModel {

    @AnnoField(
        title = "商品名称",
        search = @AnnoSearch,
        virtualColumn = true,
        edit = @AnnoEdit)
    String productName;

    @AnnoField(
        title = "商品图片",
        dataType = AnnoDataType.IMAGE,
        virtualColumn = true,
        edit = @AnnoEdit)
    String productImage;

    @AnnoField(
        title = "商品资料",
        dataType = AnnoDataType.FILE,
        virtualColumn = true,
        edit = @AnnoEdit)
    String productFile;

    @AnnoField(
        title = "商品代码",
        dataType = AnnoDataType.CODE_EDITOR,
        virtualColumn = true,
        edit = @AnnoEdit)
    String productCode;

    @AnnoField(
        title = "商品分类",
        dataType = AnnoDataType.TREE,
        virtualColumn = true,
        treeType = @AnnoTreeType(treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = BusinessProductCat.class, labelKey = "catName", pidKey = "parentId")),
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String productCatId;

    @AnnoField(
        title = "商品描述",
        virtualColumn = true,
        edit = @AnnoEdit)
    String productDesc;

    @AnnoField(
        title = "商品状态",
        virtualColumn = true,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            value = {
                @AnnoOptionType.OptionData(value = "0", label = "下架"),
                @AnnoOptionType.OptionData(value = "1", label = "上架")
            }
        ),
        search = @AnnoSearch,
        edit = @AnnoEdit)
    Integer productStatus;

    @AnnoField(
        title = "运费",
        virtualColumn = true,
        edit = @AnnoEdit)
    Long productFreight;

    /**
     * 角色按钮
     */
    @AnnoButton(name = "分类商品多对多", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinTargetClazz = BusinessProductCat.class,
        mediumTableClazz = BusinessCatProduct.class,
        mediumTargetField = "catId",
        mediumThisField = "productId",
        joinThisClazzField = "id"
    ))
    private Object roleButton;

    /**
     * 角色按钮
     */
    @AnnoButton(name = "虚拟商品多对多", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinTargetClazz = BusinessProduct.class,
        mediumTableClazz = BusinessVirtualProduct.class,
        mediumTargetField = "productId",
        mediumThisField = "productId",
        joinThisClazzField = "id"
    ))
    private Object rvButton;
}
