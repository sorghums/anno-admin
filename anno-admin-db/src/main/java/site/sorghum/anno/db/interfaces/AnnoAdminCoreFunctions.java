package site.sorghum.anno.db.interfaces;


import site.sorghum.anno.db.param.TableParam;

import java.util.function.Function;

/**
 * AnnoAdmin核心拓展服务
 * @author sorghum
 * @date 2023/09/06
 */
public class AnnoAdminCoreFunctions {

    public static Function<Class, TableParam> tableParamFetchFunction;
}
