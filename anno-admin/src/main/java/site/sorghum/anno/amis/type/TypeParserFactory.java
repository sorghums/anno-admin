package site.sorghum.anno.amis.type;

import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.amis.type.parser.*;
import site.sorghum.anno.anno.enums.AnnoDataType;

/**
 * @author Sorghum
 */
public class TypeParserFactory {
    /**
     * 获取类型解析器
     *
     * @param dataType 数据类型
     * @return {@link TypeParser}
     */
    public static TypeParser getTypeParser(AnnoDataType dataType) {
        return switch (dataType) {
            case IMAGE -> AnnoBeanUtils.getBean(ImageTypeParser.class);
            case OPTIONS -> AnnoBeanUtils.getBean(OptionsTypeParser.class);
            case PICKER -> AnnoBeanUtils.getBean(PickerTypeParser.class);
            case TREE -> AnnoBeanUtils.getBean(TreeTypeParser.class);
            case CODE_EDITOR -> AnnoBeanUtils.getBean(CodeEditorTypeParser.class);
            case FILE -> AnnoBeanUtils.getBean(FileTypeParser.class);
            default -> AnnoBeanUtils.getBean(DefaultTypeParser.class);
        };
    }
}
