package yubo.v1.datetime.standardcalendar;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Locale;

import static java.time.format.TextStyle.FULL;
import static java.time.format.TextStyle.NARROW;
import static java.time.format.TextStyle.SHORT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DayOfWeekAndMonthTest {

    @Test
    public void dayOfWeek() {
        DayOfWeek dow = DayOfWeek.FRIDAY;
        Locale locale = Locale.US;

        assertThat(dow.getDisplayName(FULL, locale), is(equalTo("Friday")));
        assertThat(dow.getDisplayName(NARROW, locale), is(equalTo("F")));
        assertThat(dow.getDisplayName(SHORT, locale), is(equalTo("Fri")));
    }

    @Test
    public void month() {
        Month month = Month.DECEMBER;
        Locale locale = Locale.US;

        assertThat(month.maxLength(), is(31));
        assertThat(month.getDisplayName(FULL, locale), is(equalTo("December")));
        assertThat(month.getDisplayName(NARROW, locale), is(equalTo("D")));
        assertThat(month.getDisplayName(SHORT, locale), is(equalTo("Dec")));
    }
}
