package site.sorghum.anno.anno.form;

import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;

@Named
public class DefaultBaseForm implements BaseForm {

    @Override
    public AnEntity getEntity() {
        return null;
    }
}
