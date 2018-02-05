package yubo.java8_impatient;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class InstantTest {

    @Test
    public void testInstantDuration() {
        final Instant a = Instant.now();
        final Instant oneDayLater = a.plus(1, ChronoUnit.DAYS);
        final Duration d = Duration.between(a, oneDayLater);

        assertThat(d.toDays(), is(1L));
        assertThat(d.toHours(), is(24L));
        assertThat(d.toMinutes(), is(1440L));
    }
}
