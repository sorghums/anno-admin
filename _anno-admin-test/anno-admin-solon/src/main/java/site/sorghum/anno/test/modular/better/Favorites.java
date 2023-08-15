package site.sorghum.anno.test.modular.better;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "收藏夹",annoTree = @AnnoTree(label = "name",parentKey = "pid",key = "id",displayAsTree = true))
@Table("better_favorites")
@AllArgsConstructor
@NoArgsConstructor
public class Favorites extends BaseMetaModel {

    @AnnoField(
        title = "收藏夹名称",
        tableFieldName = "name",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String name;

    @AnnoField(
        title = "父收藏夹",
        tableFieldName = "pid",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String pid;
}
