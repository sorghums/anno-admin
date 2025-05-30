package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoSql;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;
import site.sorghum.anno.db.QueryType;

import java.util.Date;

/**
 * 电商商品
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "商品",
    autoMaintainTable = true,
    annoLeftTree = @AnnoLeftTree(leftTreeName = "商品分类", catKey = "productCatId", treeClass = BusinessProductCat.class),
    annoTableButton = {
        @AnnoTableButton(name = "跳去百度", jumpUrl = "https://www.baidu.com/?tn=${clazz}&props=${props}"),
        @AnnoTableButton(name = "简单的JS命令", jsCmd = "alert('点击了按钮');"),
    },
    annoOrder = {@AnnoOrder(orderType = "desc", orderValue = "id")}
)
@Table("business_product")
public class BusinessProduct extends BaseMetaModel {

    @AnnoField(
        title = "商品名称",
        tableFieldName = "product_name",
        search = @AnnoSearch(queryType = QueryType.LIKE),
        edit = @AnnoEdit)
    String productName;

    @AnnoField(
        title = "商品图片",
        tableFieldName = "product_image",
        dataType = AnnoDataType.IMAGE,
        edit = @AnnoEdit)
    String productImage;

    @AnnoField(
        title = "商品头像",
        tableFieldName = "product_head_image",
        dataType = AnnoDataType.AVATAR,
        edit = @AnnoEdit)
    String productHeadImage;

    @AnnoField(
        title = "商品资料",
        tableFieldName = "product_file",
        dataType = AnnoDataType.FILE,
        edit = @AnnoEdit)
    String productFile;

    @AnnoField(
        title = "商品代码",
        tableFieldName = "product_code",
        dataType = AnnoDataType.CODE_EDITOR,
        edit = @AnnoEdit)
    String productCode;

    @AnnoField(
        title = "商品颜色",
        tableFieldName = "product_color",
        dataType = AnnoDataType.COLOR,
        edit = @AnnoEdit)
    String productColor;

    @AnnoField(
        title = "商品分类",
        tableFieldName = "product_cat_id",
        search = @AnnoSearch(queryType = QueryType.LIKE),
        dataType = AnnoDataType.TREE,
        treeType = @AnnoTreeType(anSql = @AnnoSql(sql = "SELECT id,cat_name as label,parent_id as pid FROM business_product_cat where del_flag = 0")),
        edit = @AnnoEdit(canClear = true))
    String productCatId;

    @AnnoField(
        title = "商品描述",
        dataType = AnnoDataType.RICH_TEXT,
        tableFieldName = "product_desc",
        edit = @AnnoEdit)
    String productDesc;

    @AnnoField(
        title = "商品状态",
        tableFieldName = "product_status",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
           onlineDictKey = "TRUE_FALSE"
        ),
        search = @AnnoSearch,
        edit = @AnnoEdit)
    Integer productStatus;

    @AnnoField(
        title = "运费",
        dataType = AnnoDataType.NUMBER,
        tableFieldName = "product_freight",
        search = @AnnoSearch,
        edit = @AnnoEdit(
            showBy = @AnnoEdit.ShowBy(expr = "annoDataForm.productStatus == 1")
        ))
    Long productFreight;

    @AnnoField(title = "测试日期", tableFieldName = "test_time", dataType = AnnoDataType.DATE, search = @AnnoSearch, edit = @AnnoEdit)
    private Date testDate;

    @AnnoField(title = "测试日期时间", tableFieldName = "test_date_time", dataType = AnnoDataType.DATETIME, search = @AnnoSearch, edit = @AnnoEdit)
    private Date testDateTime;

    @AnnoButton(name = "跳去百度", jumpUrl = "https://www.baidu.com/?tn=${clazz}&props=${props}")
    private Object jump2BaiduButton;


    @AnnoButton(name = "简单的JS命令", jsCmd = "alert('点击了按钮'); console.log('hello Js',props);")
    private Object jsCmd;

    /**
     * 角色按钮
     */
    @AnnoButton(name = "虚拟商品多对多", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinTargetClazz = BusinessVirtualTable.class,
        mediumTableClazz = BusinessVirtualProduct.class,
        mediumTargetField = "virtualId",
        mediumThisField = "productId",
        joinThisClazzField = "id"
    ))
    private Object rvButton;
}
