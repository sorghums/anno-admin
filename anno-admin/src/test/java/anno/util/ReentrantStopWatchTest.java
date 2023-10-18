package anno.util;

import org.junit.Test;
import site.sorghum.anno.anno.util.ReentrantStopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author songyinyin
 * @since 2023/10/18 19:04
 */
public class ReentrantStopWatchTest {

    @Test
    public void test() throws InterruptedException {
        ReentrantStopWatch stopWatch = new ReentrantStopWatch("test");
        stopWatch.start("test1");
        Thread.sleep(10);
        stopWatch.start("test10");
        Thread.sleep(20);
        stopWatch.start("test12");
        Thread.sleep(30);
        stopWatch.stop();
        stopWatch.stop();

        stopWatch.start("test2");
        Thread.sleep(10);
        stopWatch.stop();
        
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

}
