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
    String m2mMediumTableClazz;
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
     * 多对多目标的类【目标】
     */
    String m2mJoinTargetClazz;
    /**
     * 多对多目标表的字段【目标】
     */
    String m2mJoinTargetClazzField;

    /**
     * 获取多对多关联中间表本类字段对应的SQL列名
     *
     * @return 返回多对多关联中间表本类字段对应的SQL列名字符串
     * @throws BizException 当获取中间表类或字段时发生业务异常时抛出该异常
     */
    public String getM2mMediumThisFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mMediumTableClazz), m2mMediumThisField);
        } catch (BizException exception) {
            return m2mMediumThisField;
        }
    }

    /**
     * 获取多对多关联中间表目标字段对应的SQL列名
     *
     * @return 返回多对多关联中间表目标字段对应的SQL列名字符串
     * @throws BizException 当获取中间表类或字段时发生业务异常时抛出该异常
     */
    public String getM2mMediumTargetFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mMediumTableClazz), m2mMediumTargetField);
        } catch (BizException exception) {
            return m2mMediumTargetField;
        }
    }

    /**
     * 获取多对多关联本类字段对应的SQL列名
     *
     * @return 返回多对多关联本类字段对应的SQL列名字符串
     * @throws BizException 当获取本类或字段时发生业务异常时抛出该异常
     */
    public String getM2mJoinThisClazzFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mJoinThisClazz), m2mJoinThisClazzField);
        } catch (BizException exception) {
            return m2mJoinThisClazzField;
        }
    }


    /**
     * 获取多对多关联目标类字段对应的SQL列名
     *
     * @return 返回多对多关联目标类字段对应的SQL列名字符串
     * @throws BizException 当获取目标类或字段时发生业务异常时抛出该异常
     */
    public String getM2mJoinTargetClazzFieldSql() {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(AnnoFieldCache.getClazzByEntityName(m2mJoinTargetClazz), m2mJoinTargetClazzField);
        } catch (BizException exception) {
            return m2mJoinTargetClazzField;
        }
    }
}
