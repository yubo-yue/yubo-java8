package yubo.v1.datetime;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ZonedTimeTest {

    @Test
    public void testAvailableZoneIds() {
        final ZonedDateTime americaDateTime =
                ZonedDateTime.of(2016, 10, 25, 15, 55, 0, 0, ZoneId.of("America/Los_Angeles"));
        final Instant secondsSinceEpoch = americaDateTime.toInstant();
        final ZonedDateTime pstDateTime = secondsSinceEpoch.atZone(ZoneId.of("Asia/Shanghai"));
        assertThat(pstDateTime.toString(), is(equalTo("2016-10-26T06:55+08:00[Asia/Shanghai]")));

        final ZonedDateTime skipped =
                ZonedDateTime.of(LocalDate.of(2013, 3, 31), LocalTime.of(2, 30), ZoneId.of("Europe/Berlin"));
        assertThat("Daylight saving, one hour skipped", skipped.toString(), is(equalTo("2013-03-31T03:30+02:00[Europe/Berlin]")));
    }

    @Test
    public void testFlight() {
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a Z");
        final LocalDateTime leaving = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
        final ZoneId leavingZone = ZoneId.of("America/Los_Angeles");
        final ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);
        assertThat(departure.format(format),
                is(equalTo("Jul 20 2013 07:30 PM -0700")));

        // Flight is 10 hours and 50 minutes, or 650 minutes
        final ZoneId arrivingZone = ZoneId.of("Asia/Tokyo");
        assertThat(departure.withZoneSameInstant(arrivingZone).format(format),
                is(equalTo("Jul 21 2013 11:30 AM +0900")));

        ZonedDateTime arrival = departure.withZoneSameInstant(arrivingZone).plusMinutes(650);
        assertThat(arrival.format(format), is(equalTo("Jul 21 2013 10:20 PM +0900")));

        assertThat("is daylight saving",
                arrivingZone.getRules().isDaylightSavings(arrival.toInstant()),
                is(false));
    }
}
