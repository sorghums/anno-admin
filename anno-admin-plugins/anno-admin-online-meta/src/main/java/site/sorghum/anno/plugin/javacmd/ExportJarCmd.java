package site.sorghum.anno.plugin.javacmd;

import cn.hutool.core.io.FileUtil;
import jakarta.inject.Named;
import net.bytebuddy.description.type.TypeDescription;
import site.sorghum.anno.MetaClassUtil;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.plugin.ao.OnlineMeta;

import java.util.Map;

@Named
public class ExportJarCmd implements JavaCmdSupplier {

    @Override
    public String run(CommonParam param) {
        OnlineMeta onlineMeta = param.toT(OnlineMeta.class);
        String ymlContent = onlineMeta.getYmlContent();
        Map<TypeDescription, Class<?>> typeDescriptionClassMap = MetaClassUtil.loadClass(ymlContent);
        String absolutePath = FileUtil.file("dynamic.jar").
            getAbsolutePath();
        return "已导出至：%s".formatted(absolutePath);
    }
}
