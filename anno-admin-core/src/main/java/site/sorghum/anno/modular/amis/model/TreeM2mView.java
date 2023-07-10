package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.layout.Page;

import java.util.HashMap;

/**
 * 树多对多视图
 *
 * @author sorghum
 * @since 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeM2mView extends Page {

    private TreeM2mView() {

    }

    public static TreeM2mView of() {
        return treeM2mView();
    }

    private static TreeM2mView treeM2mView(){
        TreeM2mView treeM2mView = new TreeM2mView();
        Form treeForm = new Form();
        Action submit = new Action();
        submit.setBlock(true);
        submit.setLabel("提交");
        submit.setLevel("primary");
        submit.setType("submit");
        treeForm.setApi(
                new Api(){{
                    setUrl("/system/anno/${clazz}/addM2m");
                    setMethod("post");
                    setData(new HashMap<>(){{
                        put("&","$$");
                        put("ids","${m2mTree}");
                        put("_extraData","${extraData}");
                        put("clearAll","true");
                    }});
                    setMessages(new ApiMessage(){{
                        setSuccess("操作成功");
                        setFailed("操作失败");
                    }});
                }}
        );
        treeForm.setInitApi(
                new Api(){{
                    setUrl("/system/anno/${treeClazz}/annoTreeSelectData");
                    setMethod("post");
                    setData(new HashMap<>(){{
                        put("_extraData","${extraData}");
                        put("reverseM2m","false");
                    }});
                }}
        );
        treeForm.setBody(
                CollUtil.newArrayList(
                        submit,
                        new InputTree(){{
                            setId("m2mTree");
                            setName("m2mTree");
                            setAutoCheckChildren(true);
                            setMultiple(true);
                            setCascade(true);
                            setSource(
                                    new Api(){{
                                        setUrl("/system/anno/${treeClazz}/annoTrees");
                                        setMethod("post");
                                    }}
                            );
                            setHeightAuto(true);
                        }}
                )
        );
        treeForm.setWrapWithPanel(false);
        treeM2mView.setBody(
                CollUtil.newArrayList(
                        treeForm
                )
        );
        return treeM2mView;
    }
}
