package site.sorghum.anno.modular.type;

import site.sorghum.anno.common.AnnoBeanUtils;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.type.parser.CodeEditorTypeParser;
import site.sorghum.anno.modular.type.parser.DefaultTypeParser;
import site.sorghum.anno.modular.type.parser.FileTypeParser;
import site.sorghum.anno.modular.type.parser.ImageTypeParser;
import site.sorghum.anno.modular.type.parser.OptionsTypeParser;
import site.sorghum.anno.modular.type.parser.TreeTypeParser;

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
            case TREE -> AnnoBeanUtils.getBean(TreeTypeParser.class);
            case CODE_EDITOR -> AnnoBeanUtils.getBean(CodeEditorTypeParser.class);
            case FILE -> AnnoBeanUtils.getBean(FileTypeParser.class);
            default -> AnnoBeanUtils.getBean(DefaultTypeParser.class);
        };
    }
}
