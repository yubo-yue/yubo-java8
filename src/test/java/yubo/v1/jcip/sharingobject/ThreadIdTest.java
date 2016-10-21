package yubo.v1.jcip.sharingobject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ThreadIdTest {

    @Test
    public void getThreadIdConcurrently() {
        final Integer processorCount = Runtime.getRuntime().availableProcessors();

        final ExecutorService executorService =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        final List<Future<Integer>> results = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            results.add(executorService.submit(() -> ThreadId.get()));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (!executorService.isTerminated()) {
                executorService.shutdownNow();
            }
        }

        results.stream().forEach(f -> {
            try {
                assertTrue(f.get() < processorCount);
            } catch (Exception e) {
                fail();
            }
        });

        assertTrue("Successfully get here", true);
    }
}
