package site.sorghum.anno.test.modular.better;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "文章收藏夹关联表")
@Table("better_article_favorite_relation")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleFavoritesRelation extends BaseMetaModel {

    @AnnoField(
        title = "文章ID",
        tableFieldName = "article_id")
    String articleId;


    @AnnoField(
        title = "收藏夹ID",
        tableFieldName = "favorites_id")
    String favoritesId;

}
