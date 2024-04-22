package site.sorghum.anno.anno.chart;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnChart;
import site.sorghum.anno._metadata.AnChartField;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.response.AnChartResponse;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 图表服务实现类
 *
 * @author Sorghum、Qjw
 * @since 2024/02/23
 */
@Named
@Slf4j
public class AnChartServiceImpl implements AnChartService {

    @Inject
    MetadataManager metadataManager;

    @Override
    public List<AnChartResponse<Object>> getChart(String clazz, String fieldId, Map<String, Object> params) {
        AnEntity entity = metadataManager.getEntity(clazz);
        AnChart anChart = entity.getAnChart();
        if (!anChart.getEnable()) {
            log.error("实体类非图表类型或未加载!");
            return null;
        }

        List<AnChartResponse<Object>> responses = new ArrayList<>();
        List<AnChartField> chartFields = anChart.getFields().stream().filter(
            field -> field.getId().equals(fieldId) || StrUtil.isEmpty(fieldId)
        ).toList();
        for (AnChartField field : chartFields) {
            // 字段权限校验
            try {
                CheckPermissionFunction.permissionCheckFunction.accept(entity.getPermissionCode() + ":" + field.getPermissionCode());
            } catch (Exception e) {
                log.error("权限不足，无法获取{}图表！", field.getPermissionCode());
                continue;
            }

            Object result = AnnoBeanUtils.getBean(field.getRunSupplier()).get(params);
            if (Objects.nonNull(result)) {
                responses.add(new AnChartResponse<>(
                    field.getId(), field.getName(), field.getType(), result, field.getOrder()
                ));
            }
        }
        return responses;
    }
}
