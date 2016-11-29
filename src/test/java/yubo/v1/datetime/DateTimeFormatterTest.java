package yubo.v1.datetime;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateTimeFormatterTest {

    @Test
    public void testFormatAndParse() {
        final LocalDateTime birthDay = LocalDateTime.of(2016, 11, 23, 04, 00, 00, 00);
        final String stringizedBirthday = DateTimeFormatter.ISO_DATE_TIME.format(birthDay);
        final LocalDateTime confirmedBirthDay = LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(stringizedBirthday));

        assertThat(birthDay, is(equalTo(confirmedBirthDay)));
    }

    @Test
    public void testDifferentFormat() {
        final LocalDateTime birthDay = LocalDateTime.of(2016, 11, 23, 04, 00, 00, 00);
        final ZonedDateTime zonedBirthday =
                ZonedDateTime.of(LocalDate.of(2016, 11, 23), LocalTime.of(04, 00, 00, 00), ZoneId.of("Asia/Shanghai"));

        assertThat(DateTimeFormatter.BASIC_ISO_DATE.format(birthDay),
                is(equalTo("20161123")));
        assertThat(DateTimeFormatter.ISO_DATE_TIME.format(birthDay),
                is(equalTo("2016-11-23T04:00:00")));
        assertThat(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zonedBirthday),
                is(equalTo("2016-11-23T04:00:00+08:00[Asia/Shanghai]")));
        assertThat(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedBirthday),
                is(equalTo("2016-11-23T04:00:00+08:00")));
        assertThat(DateTimeFormatter.ISO_WEEK_DATE.format(birthDay),
                is(equalTo("2016-W47-3")));
        assertThat(DateTimeFormatter.ISO_WEEK_DATE.format(zonedBirthday),
                is(equalTo("2016-W47-3+08:00")));

    }

    @Test
    public void testHumanReadableFormat() {
        final LocalDateTime birthDay = LocalDateTime.of(2016, 11, 23, 04, 00, 00, 00);

        final DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        assertThat(localFormatter.format(birthDay.atZone(ZoneId.of("Asia/Shanghai"))),
                is(equalTo("Wednesday, November 23, 2016 4:00:00 AM CST")));
    }
}
