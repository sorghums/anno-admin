package site.sorghum.anno.excel;

import cn.hutool.core.util.IdUtil;
import jakarta.inject.Inject;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.anno.proxy.AnnoBaseService;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.file.AnFileService;
import site.sorghum.anno.file.FileInfo;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public abstract class AnnoExportExcelCmd implements JavaCmdSupplier {
    @Inject
    AnnoBaseService annoBaseService;

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Inject
    AnFileService anFileService;

    public abstract <T> Class<T> tClass();

    @Override
    public String run(CommonParam param) {
        AnEntity entity = metadataManager.getEntity(tClass());
        permissionProxy.checkPermission(entity, PermissionProxy.VIEW);

        Map<String, Object> mapParam = AnnoUtil.emptyStringIgnore(param);
        mapParam.remove("_extra");
        mapParam.remove("annoJavaCmdId");
        DbCriteria criteria = AnnoUtil.simpleEntity2conditions(entity, mapParam);
        List<Object> list = annoBaseService.list(criteria);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Export.writeToOutPutStream(list,entity,byteArrayOutputStream,entity.name());
        }catch (Exception e){
            throw new BizException(e.getMessage(),e);
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBytes(byteArrayOutputStream.toByteArray());
        fileInfo.setFileName(IdUtil.fastSimpleUUID()+".xlsx");
        FileInfo uploadedFile = anFileService.uploadFile(
            fileInfo
        );
        return "js://window.open('%s')".formatted(
            uploadedFile.getFileUrl()
        );
    }
}
