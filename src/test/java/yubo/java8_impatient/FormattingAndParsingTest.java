package yubo.java8_impatient;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormattingAndParsingTest {

    private ZonedDateTime apolloLaunch;

    @Before
    public void setUp() {
        apolloLaunch = ZonedDateTime.of(
                LocalDate.of(1969, 7, 16)
                , LocalTime.of(9, 32, 0, 0)
                , ZoneId.of("America/New_York")
        );
    }

    @Test
    public void testFormat() {
        final String basicIsoDateFormatted = DateTimeFormatter.BASIC_ISO_DATE.format(apolloLaunch);
        assertThat(basicIsoDateFormatted, equalTo("19690716-0400"));
        final String isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE.format(apolloLaunch);
        assertThat(isoLocalDate, equalTo("1969-07-16"));
        final String isoLocalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(apolloLaunch);
        assertThat(isoLocalDateTime, equalTo("1969-07-16T09:32:00"));
        final String isoOffsetDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(apolloLaunch);
        assertThat(isoOffsetDateTime, equalTo("1969-07-16T09:32:00-04:00"));
        final String isoZonedDateTime = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(apolloLaunch);
        assertThat(isoZonedDateTime, equalTo("1969-07-16T09:32:00-04:00[America/New_York]"));

        final String localizedDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(apolloLaunch);
        assertThat(localizedDateTime, equalTo("Wednesday, July 16, 1969 9:32:00 AM EDT"));
    }

    @Test
    public void testCustomizedFormat() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd");
        final String customizedFormatted = formatter.format(apolloLaunch);
        assertThat(customizedFormatted, is(equalTo("Wed 1969-07-16")));
    }
}
