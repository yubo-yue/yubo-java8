package yubo.v1.datetime.standardcalendar;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Set;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TimeZoneAndOffsetTest {
    @Test
    public void testTimeZones() {
        final Set<String> zones = ZoneId.getAvailableZoneIds();
        assertThat(zones, is(not(empty())));

        final LocalDateTime now = LocalDateTime.of(2016, Month.DECEMBER, 30, 16, 15, 0);
    }
}
