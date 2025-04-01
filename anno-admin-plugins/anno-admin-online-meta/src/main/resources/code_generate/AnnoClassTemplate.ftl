<#-- Import statements -->
import lombok.Data;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;
import java.util.Map;

<#-- Class definition -->
<#-- Use @Data annotation from Lombok -->
@Data
public class ${main.className} {

<#-- Fields definition -->
<#list main.fields as field>

    ${field.type} ${field.name};

</#list>

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}