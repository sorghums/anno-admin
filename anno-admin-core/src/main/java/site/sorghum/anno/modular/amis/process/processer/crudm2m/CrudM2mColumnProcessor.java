package site.sorghum.anno.modular.amis.process.processer.crudm2m;

import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.anno.modular.amis.model.CrudM2mView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CrudM2mColumnProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudM2mView crudM2mView = (CrudM2mView) amisBaseWrapper.getAmisBase();
        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        List<Map> amisColumns = new ArrayList<>();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            Table.Column column = new Table.Column();
            column.setName(field.getName());
            column.setLabel(annoField.title());
            column.setSortable(true);
            Map<String, Object> amisColumn = AnnoDataType.displayExtraInfo(column, annoField);
            if (!annoField.show()) {
                amisColumn.put("toggled", false);
            }
            amisColumns.add(amisColumn);
        }
        Crud crudBody = crudM2mView.getCrudBody();
        // 读取现有的列
        List<Map> columns = crudBody.getColumns();
        columns.addAll(0, amisColumns);
        // 重新写入
        crudBody.setColumns(columns);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
