package yubo.v1.jcip;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CountingFactorizerTest {

    @Test
    public void testAtomicIncThreadSafe() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final CountingFactorizer countingFactorizer = new CountingFactorizer();

        IntStream.range(0, 10000).forEach(i -> executorService.submit(() -> countingFactorizer.service()));

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertThat(countingFactorizer.getCount(), is(equalTo(10000L)));
    }
}
