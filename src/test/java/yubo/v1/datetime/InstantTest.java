package yubo.v1.datetime;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class InstantTest {

    @Test
    public void testInstant() {
        final Instant now = Instant.now();
        final Instant oneHourLater = now.plus(1, ChronoUnit.HOURS);

        assertThat(now.isBefore(oneHourLater), is(true));
        assertThat(oneHourLater.isAfter(now), is(true));
    }
}
