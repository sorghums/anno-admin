package site.sorghum.anno._metadata;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;
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
    public static Map<String, AnnoButtonImpl.M2MJoinButtonImpl> annoMtmMap = new HashMap<>();

    /**
     * 获取多对多关联中间表本类字段对应的SQL列名
     *
     * @return 返回多对多关联中间表本类字段对应的SQL列名字符串
     * @throws BizException 当获取中间表类或字段时发生业务异常时抛出该异常
     */
    public static String getM2mMediumThisFieldSql(AnnoButtonImpl.M2MJoinButtonImpl annoMtm) {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(annoMtm.mediumTableClazz(), annoMtm.mediumThisField());
        } catch (BizException exception) {
            return annoMtm.mediumThisField();
        }
    }

    /**
     * 获取多对多关联中间表目标字段对应的SQL列名
     *
     * @return 返回多对多关联中间表目标字段对应的SQL列名字符串
     * @throws BizException 当获取中间表类或字段时发生业务异常时抛出该异常
     */
    public static String getM2mMediumTargetFieldSql(AnnoButtonImpl.M2MJoinButtonImpl annoMtm) {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(annoMtm.mediumTableClazz(), annoMtm.mediumTargetField());
        } catch (BizException exception) {
            return annoMtm.mediumTargetField();
        }
    }

    /**
     * 获取多对多关联本类字段对应的SQL列名
     *
     * @return 返回多对多关联本类字段对应的SQL列名字符串
     * @throws BizException 当获取本类或字段时发生业务异常时抛出该异常
     */
    public static String getM2mJoinThisClazzFieldSql(Class<?> thisClass,AnnoButtonImpl.M2MJoinButtonImpl annoMtm) {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(thisClass, annoMtm.joinThisClazzField());
        } catch (BizException exception) {
            return annoMtm.joinThisClazzField();
        }
    }


    /**
     * 获取多对多关联目标类字段对应的SQL列名
     *
     * @return 返回多对多关联目标类字段对应的SQL列名字符串
     * @throws BizException 当获取目标类或字段时发生业务异常时抛出该异常
     */
    public static String getM2mJoinTargetClazzFieldSql(AnnoButtonImpl.M2MJoinButtonImpl annoMtm) {
        try {
            return AnnoFieldCache.getSqlColumnByJavaName(annoMtm.joinTargetClazz(), annoMtm.joinTargetClazzField());
        } catch (BizException exception) {
            return annoMtm.joinTargetClazzField();
        }
    }
}
