package site.sorghum.anno.om.tpl;

import cn.hutool.core.util.XmlUtil;
import site.sorghum.anno._common.util.MetaClassUtil;
import site.sorghum.anno._metadata.AnMeta;

public class DemoFunc {
    public static void main(String[] args) {
        AnMeta anMeta = MetaClassUtil.xml2AnMeta(XmlUtil.readXML(
            """
                D:\\Project\\rep\\opensource\\anno-admin\\anno-admin-plugins\\anno-admin-online-meta\\src\\main\\resources\\xml\\demo.xml               \s"""
        ));
        System.out.println(anMeta);
    }
}
