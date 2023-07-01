package site.sorghum.amis;

import com.alibaba.fastjson2.JSON;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.InputDatetime;
import site.sorghum.amis.entity.input.LocationPicker;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.amis.entity.layout.Page;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Page page = new Page();
        page.setTitle("Hello, Amis!");
        page.setSubTitle("This is a subtitle.");
        Form form = new Form();
        page.setBody(form);
        form.setBody(Arrays.asList(
                new Options() {{
                    setName("sex");
                    setOptions(
                            Arrays.asList(
                                    new Options.Option() {{
                                        setLabel("男");
                                        setValue("1");
                                    }},
                                    new Options.Option() {{
                                        setLabel("女");
                                        setValue("0");
                                    }}
                            )
                    );
                    setMultiple(true);
                }},
                new InputDatetime() {{
                    setName("birthday");
                    setLabel("生日");
                    setRequired(true);
                }},
                new LocationPicker() {{
                    setName("location");
                    setLabel("位置");
                    setRequired(true);
                }}
        ));
        System.out.println(JSON.toJSONString(page));
    }
}
