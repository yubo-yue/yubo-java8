package yubo.java8_impatient;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ZonedDateTimeTest {

    @Test
    public void instantToZoneDateTime() {
        final Instant current = Instant.now();
        System.out.println(current);
        final ZonedDateTime currentZoneDateTime = current.atZone(ZoneId.of("UTC"));
        assertThat(currentZoneDateTime.getZone(), is(ZoneId.of("UTC")));
        System.out.println(currentZoneDateTime);
    }

    @Test
    public void testDaytimeSaving() {
        final ZonedDateTime skipped = ZonedDateTime.of(LocalDate.of(2013, 3, 31), LocalTime.of(2, 30), ZoneId.of("Europe/Berlin"));
        System.out.println(skipped);

        final ZonedDateTime ambiguous = ZonedDateTime.of(
                LocalDate.of(2013, 10, 27)
                , LocalTime.of(2, 30)
                , ZoneId.of("Europe/Berlin")
        );

        final ZonedDateTime anHourLater = ambiguous.plusHours(1);

        assertThat(ambiguous.getHour(), is(anHourLater.getHour()));
        assertThat(ambiguous.getMinute(), is(anHourLater.getMinute()));
        assertThat(ambiguous.getOffset(), is(not(anHourLater.getOffset())));
    }
}
