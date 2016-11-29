package yubo.v1.datetime;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AbsoluteTimeTest {

    @Test
    public void testInstantAndDuration() throws InterruptedException {
        final Instant start = Instant.now();
        TimeUnit.SECONDS.sleep(5);
        final Instant end = Instant.now();

        final Duration shortElapsed = Duration.between(start, end);
        long millis = shortElapsed.toMillis();

        assertThat(millis > 0, is(true));
    }
}
