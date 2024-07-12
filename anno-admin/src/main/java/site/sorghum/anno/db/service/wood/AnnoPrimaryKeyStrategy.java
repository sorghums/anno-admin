package site.sorghum.anno.db.service.wood;

import lombok.extern.slf4j.Slf4j;
import org.noear.wood.wrap.PrimaryKeyStrategy;
import site.sorghum.anno.anno.util.AnnoFieldCache;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class AnnoPrimaryKeyStrategy extends PrimaryKeyStrategy {

    @Override
    public boolean fieldIsPrimaryKey(Class<?> clz, Field f) {
        String pkName = AnnoFieldCache.getPkName(clz);
        return Objects.equals(pkName, f.getName()) || super.fieldIsPrimaryKey(clz, f) ;
    }
}
