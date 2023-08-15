package site.sorghum.anno.test.modular.better;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

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
        edit = @AnnoEdit,dataType = AnnoDataType.RICH_TEXT)
    String content;

}
