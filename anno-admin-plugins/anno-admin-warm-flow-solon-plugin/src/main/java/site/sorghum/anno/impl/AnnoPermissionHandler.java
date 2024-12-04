package site.sorghum.anno.impl;

import org.dromara.warm.flow.core.handler.PermissionHandler;
import org.noear.solon.annotation.Component;

import java.util.List;

@Component
public class AnnoPermissionHandler implements PermissionHandler {

    @Override
    public List<String> permissions() {
        return List.of();
    }

    @Override
    public String getHandler() {
        return "";
    }
}
