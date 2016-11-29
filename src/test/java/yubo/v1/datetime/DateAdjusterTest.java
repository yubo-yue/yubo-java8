package yubo.v1.datetime;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateAdjusterTest {

    @Test
    public void testAdjust() {
        final LocalDate firstTuesday = LocalDate.of(2016, 10, 25);
        final LocalDate nextTues = firstTuesday.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));

        assertThat(nextTues, is(equalTo(LocalDate.of(2016, 10, 26))));
    }
}
