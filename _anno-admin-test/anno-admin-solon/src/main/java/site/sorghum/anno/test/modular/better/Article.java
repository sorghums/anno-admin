package site.sorghum.anno.test.modular.better;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.field.*;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;
import site.sorghum.anno.test.modular.better.supplier.TestTreeSupplier;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "文章")
@Table("better_article")
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseMetaModel {

    String hhName;
    @AnnoField(
        title = "文章HH名称",
        tableFieldName = "hh_name",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    public String getHhName() {
        return hhName;
    }

    String name;
    @AnnoField(
        title = "文章名称",
        tableFieldName = "name",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    public String getName() {
        return name;
    }

    @AnnoField(
        title = "文章内容",
        tableFieldName = "content",
        search = @AnnoSearch,
        edit = @AnnoEdit(placeHolder = "请输入文章内容"), dataType = AnnoDataType.CODE_EDITOR)
    String content;

    @AnnoField(
        title = "文章排序",
        tableFieldName = "order",
        search = @AnnoSearch,
        edit = @AnnoEdit(placeHolder = "请输入文章排序"), dataType = AnnoDataType.NUMBER)
    Integer order;

    @AnnoField(
        title = "测试树",
        tableFieldName = "test_tree",
        search = @AnnoSearch,
        edit = @AnnoEdit(placeHolder = "测试树"), dataType = AnnoDataType.TREE,
    treeType = @AnnoTreeType(supplier = TestTreeSupplier.class))
    String tree;


    @AnnoMany2ManyField(
        mediumTable = "better_article_favorite_relation",
        thisColumn = @AnnoJoinColumn(mediumName = "article_id", referencedName = "id"),
        otherColumn = @AnnoJoinColumn(mediumName = "favorites_id", referencedName = "id")
    )
    List<Favorites> favorites;


    @AnnoButton(name = "收藏夹", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinTargetClazz = Favorites.class,
        mediumTableClazz = ArticleFavoritesRelation.class,
        mediumTargetField = "favoritesId",
        mediumThisField = "articleId",
        joinThisClazzField = "id",
        windowSize = "sm"
    ))
    Object favoritesM2mButton;


    @AnnoButton(name = "测试TPL",annoTpl = @AnnoTpl(tplClazz = ArticleTplRender.class))
    Object helloWord;
}
