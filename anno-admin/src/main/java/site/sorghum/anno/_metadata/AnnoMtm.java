package site.sorghum.anno._metadata;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.util.AnnoFieldCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Anno多对多
 *
 * @author Sorghum
 * @since 2023/12/08
 */
@Data
@Slf4j
public class AnnoMtm {
    /**
     * 多对多映射值
     */
    public static Map<String, AnnoMtm> annoMtmMap = new HashMap<>();
    /**
     * 唯一值
     */
    String id;
    /**
     * 多对多中间类
     */
    String m2mMediumTableClass;
    /**
     * 多对多中间表的字段【本表】
     */
    String m2mMediumThisField;
    /**
     * 多对多中间表的字段【目标表】
     */
    String m2mMediumTargetField;
    /**
     * 多对多本表的类
     */
    String m2mJoinThisClazz;
    /**
     * 多对多本表的字段【本表】
     */
    String m2mJoinThisClazzField;
    /**
     * 多对多本表的类
     */
    String m2mJoinTargetClazz;
    /**
     * 多对多目标表的字段【目标】
     */
    String m2mJoinTargetClazzField;

    public String getM2mMediumThisFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mMediumTableClass), m2mMediumThisField);
        } catch (BizException exception) {
            return m2mMediumThisField;
        }
    }

    public String getM2mMediumTargetFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mMediumTableClass), m2mMediumTargetField);

        } catch (BizException exception) {
            return m2mMediumTargetField;
        }
    }

    public String getM2mJoinThisClazzFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mJoinThisClazz), m2mJoinThisClazzField);
        } catch (BizException exception) {
            return m2mJoinThisClazzField;
        }
    }


    public String getM2mJoinTargetClazzFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mJoinTargetClazz), m2mJoinTargetClazzField);
        } catch (BizException exception) {
            return m2mJoinTargetClazzField;
        }
    }
}
