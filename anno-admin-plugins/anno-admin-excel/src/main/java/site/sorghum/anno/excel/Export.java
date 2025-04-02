package site.sorghum.anno.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Excel导出工具类
 * 提供将数据列表导出到Excel的功能
 */
@Slf4j
public class Export {

    /**
     * 将数据写入输出流，生成Excel文件
     *
     * @param dataList         数据列表
     * @param anEntity      实体元数据
     * @param outputStream  输出流
     * @param titleName     Excel标题名称
     * @param ignoreColumns 需要忽略的列名
     */
    @SneakyThrows
    public static void writeToOutPutStream(
        List<Object> dataList,
        AnEntity anEntity,
        OutputStream outputStream,
        String titleName,
        String... ignoreColumns
    ) {
        // 创建ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<String> ignoreColumnList = Arrays.asList(ignoreColumns);

        // 设置表头别名
        for (AnField field : anEntity.getFields()) {
            if (!CollUtil.contains(ignoreColumnList, field.getJavaName())) {
                writer.addHeaderAlias(field.getJavaName(), field.getTitle());
            }
        }

        // 写入数据
        writer.setOnlyAlias(true);
        writer.merge(writer.getHeaderAlias().size() - 1, titleName);
        writer.write(dataList, true);

        // 刷新并关闭资源
        writer.flush(outputStream, true);
        writer.close();
    }
}