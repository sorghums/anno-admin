package site.sorghum.anno.plugin.ao;

import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.anno.anno.proxy.field.ZeroFiledBaseSupplier;
import site.sorghum.anno.trans.OnlineDictCache;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class AnDictBase{


    @AnnoField(pkField = true, title = "主键",
        tableFieldName = "id",
        show = false,
        insertWhenNullSet = SnowIdSupplier.class,
        fieldSize = 32)
    String id;

    /**
     * 父级ID
     */
    String pid;

    @AnnoField(pkField = true, title = "字典值",
        tableFieldName = "value",
        edit = @AnnoEdit,
        fieldSize = 256)
    String value;

    @AnnoField(
        title = "字典名称",
        edit = @AnnoEdit
    )
    String name;

    @AnnoField(
        title = "颜色",
        dataType = AnnoDataType.COLOR,
        edit = @AnnoEdit(canClear = true)
    )
    String color;

    @AnnoField(title = "创建人", tableFieldName = "create_by", show = false, fieldSize = 32)
    private String createBy;

    @AnnoField(title = "创建时间", tableFieldName = "create_time", dataType = AnnoDataType.DATETIME, show = false)
    private LocalDateTime createTime;

    @AnnoField(title = "更新人", tableFieldName = "update_by", show = false, fieldSize = 32)
    private String updateBy;

    @AnnoField(title = "更新时间", tableFieldName = "update_time", dataType = AnnoDataType.DATETIME, show = false)
    private LocalDateTime updateTime;

    /**
     * 状态 1 正常 0 封禁
     */
    @AnnoField(title = "删除标识", tableFieldName = "del_flag",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "已删除", value = "1"),
            @AnnoOptionType.OptionData(label = "正常", value = "0")
        }), show = false, fieldSize = 1, insertWhenNullSet = ZeroFiledBaseSupplier.class)
    private Integer delFlag;

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();

    /**
     * 转换为在线字典
     * @return OnlineDict
     */
    public static List<OnlineDictCache.OnlineDict> toDict(List<AnDictBase> anDictBases){
        // anDictBases 以id为key，AnDictBase为value 存入临时map
        Map<String, AnDictBase> anDictBaseMap = anDictBases.stream().collect(Collectors.toMap(
            AnDictBase::getId,
            a -> a
        ));
        return anDictBases.stream().map(
            it -> {
                OnlineDictCache.OnlineDict onlineDict = new OnlineDictCache.OnlineDict();
                AnDictBase parent = anDictBaseMap.getOrDefault(it.getPid(),null);
                onlineDict.setId(it.getId());
                onlineDict.setName(it.getName());
                onlineDict.setValue(it.getValue());
                onlineDict.setColor(it.getColor());
                if (parent != null) {
                    onlineDict.setParentValue(parent.getValue());
                    onlineDict.setPid(parent.getId());
                }
                return onlineDict;
            }
        ).toList();

    }
}
