package site.sorghum.anno.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import site.sorghum.anno.anno.annotation.global.AnnoScan;

/**
 * @author Sorghum
 */
@SpringBootApplication
@AnnoScan(scanPackage = "site.sorghum.anno")
public class AnnoSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnnoSpringBootApplication.class, args);
    }
}
