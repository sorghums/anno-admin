package site.sorghum.anno.utils;

import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.db.BaseMetaModel;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.PrimaryKeyModel;
import site.sorghum.anno.suppose.model.BaseOrgMetaModel;

/**
 * @author songyinyin
 * @since 2024/1/17 20:46
 */
public class MTUtils {

    public static boolean instanceofBaseMetaModel(Object arg) {
        if (arg instanceof DbCriteria criteria) {
            AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(criteria.getEntityName());
            return BaseMetaModel.class.isAssignableFrom(entity.getThisClass());
        } else {
            return arg instanceof BaseMetaModel;
        }
    }

    public static boolean instanceofBaseOrgMetaModel(Object arg) {
        if (arg instanceof DbCriteria criteria) {
            AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(criteria.getEntityName());
            return BaseOrgMetaModel.class.isAssignableFrom(entity.getThisClass());
        } else {
            return arg instanceof BaseOrgMetaModel;
        }
    }

    public static boolean instanceofPrimaryKeyModel(Object arg) {
        if (arg instanceof DbCriteria criteria) {
            AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(criteria.getEntityName());
            return PrimaryKeyModel.class.isAssignableFrom(entity.getThisClass());
        } else {
            return arg instanceof PrimaryKeyModel;
        }
    }
}
