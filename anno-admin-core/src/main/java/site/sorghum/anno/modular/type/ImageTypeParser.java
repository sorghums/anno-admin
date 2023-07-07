package site.sorghum.anno.modular.type;

import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Image;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;

import java.util.Map;

/**
 * @author Sorghum
 */
@Component
public class ImageTypeParser implements TypeParser{

    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnnoField annoField) {
        Image image = new Image();
        AnnoImageType imageType = annoField.imageType();
        if (imageType.height() > 0 && imageType.width() > 0){
            image.setHeight(imageType.height());
            image.setWidth(imageType.width());
        }
        image.setEnlargeAble(imageType.enlargeAble());
        image.setThumbMode(imageType.thumbMode().getMode());
        image.setThumbRatio(imageType.thumbRatio().getRatio());
        return mergeObj(image,amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnnoField annoField) {
        return formItem;
    }

}
