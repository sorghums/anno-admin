package site.sorghum.anno.modular.type;

import org.noear.solon.Solon;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.type.parser.*;

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
            case IMAGE -> Solon.context().getBean(ImageTypeParser.class);
            case OPTIONS -> Solon.context().getBean(OptionsTypeParser.class);
            case TREE -> Solon.context().getBean(TreeTypeParser.class);
            case CODE_EDITOR -> Solon.context().getBean(CodeEditorTypeParser.class);
            default -> Solon.context().getBean(DefaultTypeParser.class);
        };
    }
}
