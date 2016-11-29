package yubo.v1.datetime;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DateTimeTest {
    @Test
    public void testLocalDate() {
        final LocalDate date = LocalDate.of(1982, Month.NOVEMBER, 23);
        final LocalDate nextWednesday = date.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        final DayOfWeek dayOfWeek = date.getDayOfWeek();

        assertThat(date.isBefore(nextWednesday), is(true));
        assertThat(nextWednesday, is(equalTo(LocalDate.of(1982, Month.NOVEMBER, 24))));
        assertThat(dayOfWeek, is(equalTo(DayOfWeek.TUESDAY)));
    }

    @Test
    public void testYearMonth() {
        final YearMonth date = YearMonth.of(2016, Month.OCTOBER);
        final YearMonth feb = YearMonth.of(2016, Month.FEBRUARY);

        assertThat(date.lengthOfMonth(), is(31));
        assertThat(date.lengthOfYear(), is(366));
        assertThat(date.isLeapYear(), is(true));
        assertThat(date.isBefore(YearMonth.of(2016, Month.APRIL)), is(false));

        assertThat(feb.lengthOfMonth(), is(29));
    }

    @Test
    public void testMonthDay() {
        final MonthDay date = MonthDay.of(Month.FEBRUARY, 29);

        assertThat(date.isValidYear(2010), is(false));
        assertThat(date.isValidYear(2016), is(true));
    }

    @Test
    public void testYear() {
        assertThat(Year.of(2016).isLeap(), is(true));
        assertThat(Year.of(2010).isLeap(), is(false));
    }

    @Test
    public void testProgrammerDay() {
        final LocalDate programmersDay = LocalDate.of(2014, 1, 1).plusDays(255);
        final LocalDate birthday = LocalDate.of(1982, 11, 23);
        final LocalDate nextBirthday = birthday.plus(Period.ofYears(1));

        assertThat(nextBirthday, is(equalTo(LocalDate.of(1983, 11, 23))));
    }

    @Test
    public void testUntil() {
        final LocalDate programmersDay = LocalDate.of(2014, 10, 24);
        final LocalDate birthday = LocalDate.of(2014, 11, 23);
        final long untilDays = programmersDay.until(birthday, ChronoUnit.DAYS);

        assertThat(untilDays, is(equalTo(30L)));
    }

    @Test
    public void testLocalDateDiff() {
        final LocalDate current = LocalDate.of(2014, 10, 24);
        final LocalDate oneMonthLater = current.plusMonths(1);
        assertThat(oneMonthLater, is(equalTo(LocalDate.of(2014, 11, 24))));
    }
}
