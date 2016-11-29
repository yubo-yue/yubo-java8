package yubo.v1.datetime;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DayOfWeekAndMonthTest {

    @Test
    public void testDayOfWeek() {
        final DayOfWeek dow = DayOfWeek.FRIDAY;
        final Locale locale = Locale.US;

        assertThat(dow.plus(2), is(equalTo(DayOfWeek.SUNDAY)));
        assertThat(dow.getDisplayName(TextStyle.FULL, locale), is(equalTo("Friday")));
        assertThat(dow.getDisplayName(TextStyle.NARROW, locale), is(equalTo("F")));
        assertThat(dow.getDisplayName(TextStyle.SHORT, locale), is(equalTo("Fri")));
    }

    @Test
    public void testMonth() {
        final Month month = Month.AUGUST;
        final Locale locale = Locale.CHINA;

        assertThat(month.plus(2), is(equalTo(Month.OCTOBER)));
        assertThat(month.getDisplayName(TextStyle.FULL, locale), is(equalTo("八月")));
    }
}
