package site.sorghum.anno.excel;

import cn.hutool.core.util.IdUtil;
import jakarta.inject.Inject;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.file.AnFileService;
import site.sorghum.anno.file.FileInfo;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 自定义Excel导出命令抽象类
 * 提供更灵活的Excel导出功能，不依赖于特定实体类
 */
public abstract class ExtraExportExcelCmd implements JavaCmdSupplier {

    @Inject
    AnFileService anFileService;

    /**
     * 获取工作表名称
     * @return 工作表名称
     */
    public abstract String sheetName();

    /**
     * 获取要导出的数据列表
     * @param param 公共参数
     * @return 数据列表
     */
    public abstract <T> List<T> datas(CommonParam param);

    /**
     * 获取表头映射关系
     * @return 键为字段名，值为表头显示名称的映射
     */
    public abstract LinkedHashMap<String, String> headerAlias();

    @Override
    public String run(CommonParam param) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // 构建虚拟实体结构
            AnEntity anEntity = new AnEntity();
            anEntity.setColumns(
                headerAlias().entrySet().stream().map(entry -> {
                    AnField anField = new AnField();
                    anField.setJavaName(entry.getKey());
                    anField.setJsonPath(entry.getKey());
                    anField.setTitle(entry.getValue());
                    return anField;
                }).toList()
            );

            // 导出Excel
            Export.writeToOutPutStream(datas(param), anEntity, byteArrayOutputStream, sheetName());
        } catch (Exception e) {
            throw new BizException("自定义Excel导出失败: " + e.getMessage(), e);
        }

        // 上传文件并返回下载链接
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBytes(byteArrayOutputStream.toByteArray());
        fileInfo.setFileName(IdUtil.fastSimpleUUID() + ".xlsx");
        FileInfo uploadedFile = anFileService.uploadFile(fileInfo);

        return "js://window.open('%s')".formatted(uploadedFile.getFileUrl());
    }
}