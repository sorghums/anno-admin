package site.sorghum.anno.impl;

import jakarta.inject.Named;
import org.dromara.warm.flow.core.handler.PermissionHandler;

import java.util.List;

@Named
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
