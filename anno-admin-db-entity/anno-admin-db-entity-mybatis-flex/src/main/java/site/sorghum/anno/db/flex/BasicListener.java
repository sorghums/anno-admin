package site.sorghum.anno.db.flex;

import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.db.BaseMetaModel;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * 基本侦听器
 *
 * @author sorghum
 * @since 2023/08/29
 */
@Slf4j
public class BasicListener implements InsertListener, UpdateListener {

    public static Supplier<String> userIdSupplier = () -> "";

    @Override
    public void onInsert(Object model) {
        if (model instanceof BaseMetaModel basicModel) {
            basicModel.setCreateBy(userIdSupplier.get());
            basicModel.setUpdateBy(userIdSupplier.get());
            basicModel.setCreateTime(LocalDateTime.now());
            basicModel.setUpdateTime(LocalDateTime.now());
        }
    }

    @Override
    public void onUpdate(Object entity) {
        if (entity instanceof BaseMetaModel basicModel) {
            basicModel.setUpdateBy(userIdSupplier.get());
            basicModel.setUpdateTime(LocalDateTime.now());
        }
    }
}
