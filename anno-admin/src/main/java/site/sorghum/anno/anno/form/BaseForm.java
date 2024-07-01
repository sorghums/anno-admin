package site.sorghum.anno.anno.form;


import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

/**
 * 基础表单
 */
public interface BaseForm {

    /**
     * 获取当前对象的实体类对象
     *
     * @return 返回当前对象的实体类对象
     */
    default AnEntity getEntity() {
        return AnnoBeanUtils.getBean(MetadataManager.class).loadFormEntity(this.getClass());
    }
}
