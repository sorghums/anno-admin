package site.sorghum.anno.modular.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Anno模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Data @EqualsAndHashCode(callSuper = false)
@Slf4j
public class AnnoModule implements Runnable {

    /**
     * 模块名称
     */
    public String modelName;

    /**
     * 模块描述
     */
    public String modelDesc;

    public AnnoModule(String modelName, String modelDesc) {
        this.modelName = modelName;
        this.modelDesc = modelDesc;
    }

    @Override
    public void run() {
        printModelInfo();
    }

    public void printModelInfo(){
        log.info("【🚀🚀🚀 ===> AnnoModule: {}, desc: {} 】", modelName, modelDesc);
    }
}
