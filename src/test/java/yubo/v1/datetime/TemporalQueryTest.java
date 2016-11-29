package yubo.v1.datetime;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TemporalQueryTest {

    @Test
    public void testQuery() {
        final TemporalQuery<TemporalUnit> query = TemporalQueries.precision();
        assertThat(ChronoUnit.DAYS, is(equalTo(LocalDate.now().query(query))));
        assertThat(ChronoUnit.YEARS, is(equalTo(Year.now().query(query))));
    }

}
