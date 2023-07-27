package site.sorghum.anno.modular.type.parser;

import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Image;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.modular.type.TypeParser;

import java.util.Map;

/**
 * @author Sorghum
 */
@Component
public class ImageTypeParser implements TypeParser {

    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Image image = new Image();
        if (anField.getImageHeight() > 0 && anField.getImageWidth() > 0){
            image.setHeight(anField.getImageHeight());
            image.setWidth(anField.getImageWidth());
        }
        image.setEnlargeAble(anField.isImageEnlargeAble());
        image.setThumbMode(anField.getImageThumbMode().getMode());
        image.setThumbRatio(anField.getImageThumbRatio().getRatio());
        return mergeObj(image,amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        return formItem;
    }

}
