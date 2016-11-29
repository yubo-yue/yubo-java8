package yubo.v1.datetime;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LocalTimeTest {

    @Test
    public void testLocalTime() {
        final LocalTime time = LocalTime.of(6, 5);

        assertThat(time.getHour(), is(6));
        assertThat(time.getMinute(), is(5));
        assertThat(time.getSecond(), is(0));
    }

    @Test
    public void testLocalDateTime() {
        final LocalDateTime dateTime = LocalDateTime.of(1994, Month.APRIL, 15, 11, 30);

        assertThat(dateTime.plusMonths(6),
                is(equalTo(LocalDateTime.of(1994, Month.OCTOBER, 15, 11, 30))));
    }
}
