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

@Slf4j
public class Export {

    /**
     * 写入输出流
     *
     * @param datas        数据
     * @param anEntity     一个实体
     * @param outputStream 输出流
     * @param titleName    标题名称
     * @param ignoreColumn 忽略列
     */
    @SneakyThrows
    public static void writeToOutPutStream(
        List<Object> datas,
        AnEntity anEntity,
        OutputStream outputStream,
        String titleName,
        String ...ignoreColumns
    ){
        // 获取ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<String> ignoreColumnList = Arrays.stream(ignoreColumns).toList();
        List<AnField> fields = anEntity.getFields();
        for (AnField field : fields) {
            // 忽略跳过
            if (CollUtil.contains(ignoreColumnList, field.getJavaName())) {
                continue;
            }
            writer.addHeaderAlias(field.getJavaName(), field.getTitle());
        }
        writer.setOnlyAlias(true);
        writer.merge(writer.getHeaderAlias().size() - 1, titleName);
        writer.write(datas,true);
        writer.flush(outputStream,true);
        writer.close();
    }
}
