package site.sorghum.anno.plugin.option;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.util.List;
import java.util.Map;

@Named
@Slf4j
public class AnEntityFieldSupplier implements OptionDataSupplier {

    @Inject
    MetadataManager metadataManager;

    public AnEntityFieldSupplier() {
        init();
    }

    @Override
    public List<AnnoOptionTypeImpl.OptionDataImpl> getOptionDataList() {
        Map<String, Object> requestParams = AnnoContextUtil.getContext().getRequestParams();
        log.info("[AnEntityFieldSupplier] requestParams:{}", requestParams);
        return metadataManager.getAllEntity().stream().map(
            it-> new AnnoOptionTypeImpl.OptionDataImpl(it.getName(), it.getEntityName())
        ).toList();
    }
}
