package site.sorghum.anno.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.pf4j.Pf4jRunner;

import java.nio.file.Path;

/**
 * @author Sorghum
 */
@SpringBootApplication
@AnnoScan(scanPackage = "site.sorghum.anno")
public class AnnoSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnnoSpringBootApplication.class, args);
        Pf4jRunner.runPlugin(Path.of("D:\\Project\\rep\\anno-admin-demo-p4j-plugin\\target\\anno-admin-demo-p4j-plugin-1.0-SNAPSHOT.jar"));
        Pf4jRunner.runPlugin(Path.of("D:\\Project\\rep\\opensource\\anno-admin\\anno-admin-plugins\\anno-admin-online-meta"));
        System.out.println("");
    }
}
