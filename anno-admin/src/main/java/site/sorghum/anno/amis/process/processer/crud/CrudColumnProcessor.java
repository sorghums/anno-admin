package site.sorghum.anno.amis.process.processer.crud;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudColumnProcessor implements BaseProcessor {
    @Inject
    MetadataManager metadataManager;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<AnField> fields = anEntity.getFields();
        List<Map> amisColumns = new ArrayList<>();
        for (AnField field : fields) {
            Table.Column column = new Table.Column();
            column.setName(field.getFieldName());
            column.setLabel(field.getTitle());
            column.setSortable(true);
            Map<String, Object> amisColumn = AnnoDataType.displayExtraInfo(column, field);
            if (!field.isShow()) {
                amisColumn.put("toggled", false);
            }
            amisColumns.add(amisColumn);
        }
        Crud crudBody = crudView.getCrudBody();
        // 读取现有的列
        List<Map> columns = crudBody.getColumns();
        columns.addAll(0, amisColumns);
        // 重新写入
        crudBody.setColumns(columns);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
