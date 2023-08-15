package site.sorghum.anno.amis.type.parser;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Image;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.amis.type.TypeParser;

import java.util.Map;

/**
 * @author Sorghum
 */
@Named
public class ImageTypeParser implements TypeParser {

    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Image image = new Image();
        if (anField.getImageHeight() > 0 && anField.getImageWidth() > 0) {
            image.setHeight(anField.getImageHeight());
            image.setWidth(anField.getImageWidth());
        }
        image.setEnlargeAble(anField.isImageEnlargeAble());
        image.setThumbMode(anField.getImageThumbMode().getMode());
        image.setThumbRatio(anField.getImageThumbRatio().getRatio());
        return mergeObj(image, amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        return formItem;
    }

}
