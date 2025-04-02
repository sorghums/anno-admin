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

/**
 * 基于实体类的Excel导出命令抽象类
 * 提供基于Anno框架实体类的通用Excel导出功能
 */
public abstract class AnnoExportExcelCmd implements JavaCmdSupplier {
    @Inject
    AnnoBaseService annoBaseService;

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Inject
    AnFileService anFileService;

    /**
     * 获取实体类Class对象
     * @return 实体类Class
     */
    public abstract <T> Class<T> tClass();

    @Override
    public String run(CommonParam param) {
        // 获取实体元数据并检查权限
        AnEntity entity = metadataManager.getEntity(tClass());
        permissionProxy.checkPermission(entity, PermissionProxy.VIEW);

        // 准备查询条件
        Map<String, Object> mapParam = AnnoUtil.emptyStringIgnore(param);
        mapParam.remove("_extra");
        mapParam.remove("annoJavaCmdId");
        DbCriteria criteria = AnnoUtil.simpleEntity2conditions(entity, mapParam);

        // 查询数据并导出到Excel
        List<Object> list = annoBaseService.list(criteria);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Export.writeToOutPutStream(list, entity, byteArrayOutputStream, entity.name());
        } catch (Exception e) {
            throw new BizException("Excel导出失败: " + e.getMessage(), e);
        }

        // 上传Excel文件并返回下载链接
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBytes(byteArrayOutputStream.toByteArray());
        fileInfo.setFileName(IdUtil.fastSimpleUUID() + ".xlsx");
        FileInfo uploadedFile = anFileService.uploadFile(fileInfo);

        return "js://window.open('%s')".formatted(uploadedFile.getFileUrl());
    }
}