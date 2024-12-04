package site.sorghum.anno.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import site.sorghum.anno.anno.interfaces.PrimaryKeyModelInterfaces;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/8/18 19:01
 */
@Data
public class PrimaryKeyModel implements Serializable, PrimaryKeyModelInterfaces {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected String id;

    @JoinResMap
    @TableField(exist = false)
    Map<String, Object> joinResMap = new HashMap<>();
}
