package yubo.v1.datetime.standardcalendar;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateTest {

    @Test
    public void testLocalDate() {
        LocalDate date = LocalDate.of(2016, Month.DECEMBER, 30);
        LocalDate nextWed = date.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));

        assertThat(date.getYear(), is(equalTo(2016)));
        assertThat(date.getMonth(), is(equalTo(Month.DECEMBER)));
        assertThat(date.getDayOfMonth(), is(equalTo(30)));

        assertThat(date.isBefore(nextWed), is(true));
    }
}
