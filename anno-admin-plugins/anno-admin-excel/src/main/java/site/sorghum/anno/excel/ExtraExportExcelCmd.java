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

public abstract class ExtraExportExcelCmd implements JavaCmdSupplier {

    @Inject
    AnFileService anFileService;

    /**
     * 获取工作表名称
     *
     * @return 返回工作表的名称
     */
    public abstract String sheetName();

    /**
     * 获取数据列表
     *
     * @param param 公共参数
     * @return 数据列表
     */
    public abstract <T> List<T> datas(CommonParam param);

    /**
     * 获取请求头别名的映射关系
     *
     * @return 包含请求头别名的映射关系，键为原始请求头名称，值为别名
     */
    public abstract LinkedHashMap<String, String> headerAlias();

    @Override
    public String run(CommonParam param) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            AnEntity anEntity = new AnEntity();
            anEntity.setColumns(
                headerAlias().entrySet().stream().map(
                    entry -> {
                        AnField anField = new AnField();
                        anField.setJavaName(entry.getKey());
                        anField.setTitle(entry.getValue());
                        return anField;
                    }
                ).toList()
            );
            Export.writeToOutPutStream(datas(param), anEntity, byteArrayOutputStream, sheetName());
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBytes(byteArrayOutputStream.toByteArray());
        fileInfo.setFileName(IdUtil.fastSimpleUUID() + ".xlsx");
        FileInfo uploadedFile = anFileService.uploadFile(
            fileInfo
        );
        return "js://window.open('%s')".formatted(
            uploadedFile.getFileUrl()
        );
    }
}