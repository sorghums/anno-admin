package site.sorghum.anno.test.modular.better;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.*;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "文章")
@Table("better_article")
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseMetaModel {

    @AnnoField(
        title = "文章名称",
        tableFieldName = "name",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String name;


    @AnnoField(
        title = "文章内容",
        tableFieldName = "content",
        edit = @AnnoEdit, dataType = AnnoDataType.RICH_TEXT)
    String content;


    @AnnoMany2ManyField(
        mediumTable = "better_article_favorite_relation",
        thisColumn = @AnnoJoinColumn(mediumName = "article_id", referencedName = "id"),
        otherColumn = @AnnoJoinColumn(mediumName = "favorites_id", referencedName = "id")
    )
    List<Favorites> favorites;


    @AnnoButton(name = "收藏夹", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinAnnoMainClazz = Favorites.class,
        mediumTableClass = ArticleFavoritesRelation.class,
        mediumOtherField = "favorites_id",
        mediumThisField = "article_id",
        joinThisClazzField = "id"
    ))
    Object favoritesM2mButton;
}
