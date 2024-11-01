package site.sorghum.anno.om.tpl;

import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.util.List;
import java.util.Map;

public class DemoFunc {
    public static void main(String[] args) {
        MainConfig mainConfig = new MainConfig();
        mainConfig.setName("测试");
        mainConfig.setTableName("test_main");
        mainConfig.setClassName("TestMain");
        mainConfig.setCanRemove(true);
        mainConfig.setAutoMaintainTable(true);
        mainConfig.setFields(
            List.of(
                new MainConfig.FieldConfig(){{
                    setType("String");
                    setName("testColumn");
                    setTitle("测试列");
                    setTableFieldName("test_column");
                    setDataType(AnnoDataType.OPTIONS);
                    setEdit(new MainConfig.EditConfig());
                    setFieldSize(256);
                    setSearch(new MainConfig.SearchConfig());
                    setFileType(new MainConfig.FileType());
                    setCodeType(new MainConfig.CodeType());
                    setImageType(new MainConfig.ImageType());
                    setTreeType(new MainConfig.TreeType(){{
                        setTreeAnnoClass("site.sorghum.anno.om.ao.OnlineMeta");
                        setValue(List.of(
                            new MainConfig.TreeType.TreeData(){{
                                setId("1");
                                setPid("0");
                                setLabel("测试1");
                            }}
                        ));
                    }});
                    setOptionType(
                        new MainConfig.OptionType(){{
                            setValue(List.of(
                                new MainConfig.OptionType.OptionData(){{
                                    setValue("1");
                                    setLabel("测试1");
                                }}
                            ));
                            setOptionAnnoClass("site.sorghum.anno.om.ao.OnlineMeta");
                        }}
                    );
                }}
            )
        );

        // freemarker手动渲染 AnnoMainTemplate.ftl 到控制台打印
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("code_generate", TemplateConfig.ResourceMode.CLASSPATH));
        String render =engine.getTemplate("AnnoMainTemplate.ftl").render(Map.of(
            "main", mainConfig
        ));
        System.out.println(render);
    }
}
