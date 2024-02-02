package site.sorghum.anno.plugin.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnChart;
import site.sorghum.anno._metadata.AnChartField;
import site.sorghum.anno.plugin.entity.response.AnChartResponse;
import site.sorghum.anno.plugin.service.AnChartService;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@Slf4j
public class AnChartServiceImpl implements AnChartService {

    @Inject
    AuthService authService;

    @Override
    public List<AnChartResponse<Object>> getChart(String clazz) {
        AnChart anChart = AnChart.chartMap.get(clazz);
        if (Objects.isNull(anChart)) {
            log.error("未查询到{}图表！", clazz);
            return null;
        }

        List<AnChartResponse<Object>>  responses = new ArrayList<>();
        for (AnChartField field : anChart.getFields()) {
            // 字段权限校验
            try {
                authService.verifyPermission(anChart.getPermissionCode() + ":" + field.getPermissionCode());
            } catch (Exception e) {
                log.error("权限不足，无法获取{}图表！", field.getPermissionCode());
                continue;
            }

            Object result = AnnoBeanUtils.getBean(field.getRunSupplier()).get();
            if (Objects.nonNull(result)){
                responses.add(new AnChartResponse<>(
                    field.getName(), field.getType(), result, field.getOrder()
                ));
            }
        }
        return responses;
    }
}
