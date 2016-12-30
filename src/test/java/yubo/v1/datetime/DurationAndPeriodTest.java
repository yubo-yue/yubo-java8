package yubo.v1.datetime;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DurationAndPeriodTest {
    @Test
    public void testDuration() {
        Instant instant = Instant.now();
        Duration gap = Duration.ofSeconds(10);

        Instant later = instant.plus(gap);
        long ns = Duration.between(instant, later).get(ChronoUnit.SECONDS);

        assertThat(ns, is(equalTo(10L)));
    }

    @Test
    public void testPeriod() {
        final LocalDate today = LocalDate.of(2016, Month.OCTOBER, 26);
        final LocalDate birthday = LocalDate.of(1960, Month.JANUARY, 1);

        Period period = Period.between(birthday, today);
        long p2 = ChronoUnit.DAYS.between(birthday, today);

        assertThat(period.getYears(), is(equalTo(56)));
        assertThat(period.getMonths(), is(equalTo(9)));
        assertThat(period.getDays(), is(equalTo(25)));
        assertThat(p2, is(equalTo(20753L)));
    }
}
