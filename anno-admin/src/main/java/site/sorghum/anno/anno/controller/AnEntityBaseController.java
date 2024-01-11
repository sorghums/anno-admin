package site.sorghum.anno.anno.controller;

import jakarta.inject.Inject;
import org.noear.snack.ONode;
import org.noear.snack.core.Feature;
import org.noear.snack.core.Options;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
public class AnEntityBaseController {

    @Inject
    protected MetadataManager metadataManager;


    private static final String SPLIT = "\\.";
    private static final Options OPTIONS;

    static {
        OPTIONS = Options.def();
        OPTIONS.addEncoder(Class.class,(data, node)  -> {
            if (data == null) {
                node.val().set(null);
                return;
            } else {
                String[] names = data.getName().split(SPLIT);
                node.val().set(names[names.length - 1]);
            }
        });
        OPTIONS.add(Feature.EnumUsingName);

    }

    public AnnoResult<Map<Object,Object>> anEntity(String clazz){
        AnEntity entity = metadataManager.getEntity(clazz);
        if (entity != null){
            return AnnoResult.succeed(ONode.load(entity, OPTIONS).toObject(Map.class));
        }
        return AnnoResult.succeed();
    }
}
