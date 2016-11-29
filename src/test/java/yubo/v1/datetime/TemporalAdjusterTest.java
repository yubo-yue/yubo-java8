package yubo.v1.datetime;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TemporalAdjusterTest {
    @Test
    public void testAdjust() {
        final LocalDate date = LocalDate.of(2000, Month.OCTOBER, 15);
        final DayOfWeek dotw = date.getDayOfWeek();
        assertThat(dotw, is(equalTo(DayOfWeek.SUNDAY)));

        assertThat(date.with(TemporalAdjusters.firstDayOfMonth()),
                is(equalTo(LocalDate.of(2000, Month.OCTOBER, 1))));
        assertThat(date.with(TemporalAdjusters.firstDayOfNextMonth()),
                is(equalTo(LocalDate.of(2000, Month.NOVEMBER, 1))));
        assertThat(date.with(TemporalAdjusters.lastDayOfMonth()),
                is(equalTo(LocalDate.of(2000, Month.OCTOBER, 31))));
        assertThat(date.with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                is(equalTo(LocalDate.of(2000, Month.OCTOBER, 16))));
        assertThat(date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)),
                is(equalTo(LocalDate.of(2000, Month.OCTOBER, 16))));
        assertThat(date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)),
                is(equalTo(LocalDate.of(2000, Month.OCTOBER, 13))));
    }
}
