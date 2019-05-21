package yubo.java8_impatient;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LocalDateTest {
    @Test
    public void testLocalDate() {
        final LocalDate alonzosBirthday = LocalDate.of(1903, 6, 14);
        final LocalDate alonzosBirthdayCodpy = LocalDate.of(1903, Month.JUNE, 14);

        assertThat(alonzosBirthday, equalTo(alonzosBirthdayCodpy));
    }

    /**
     * Programmer's day is 256th day of year.
     */
    @Test
    public void testProgrammerDay() {
        final LocalDate programmerDay = LocalDate.of(2014, 1, 1).plusDays(255);
        final LocalDate expectedProgrammerDay = LocalDate.of(2014, Month.SEPTEMBER, 13);
        System.out.println(programmerDay);

        assertThat(programmerDay, is(equalTo(expectedProgrammerDay)));
    }

    @Test
    public void testLocalDateAndPeriod() {
        final LocalDate birthDay = LocalDate.of(1982, Month.NOVEMBER, 23);
        final LocalDate nextBirthDay = birthDay.plusYears(36);
        System.out.println(nextBirthDay);

        final LocalDate now = LocalDate.now();
        final Period periodToNextBirthday = now.until(nextBirthDay);
        System.out.println(periodToNextBirthday.getMonths());

        final Year futureYear = Year.of(2020);


        assertThat(nextBirthDay.getMonth(), is(Month.NOVEMBER));
        assertThat(nextBirthDay.getDayOfMonth(), is(23));
    }

    @Test
    public void testTemporalAdjuster() {
        final LocalDate newYear = LocalDate.of(2018, Month.JANUARY, 1);
        final LocalDate firstMonday = newYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));

        assertThat(firstMonday, is(LocalDate.of(2018, Month.JANUARY, 2)));
    }

    @Test
    public void testNextWorkDay() {
        final LocalDate today = LocalDate.of(2018, 1, 20);
        TemporalAdjuster nextWorkdayAdjuster = TemporalAdjusters.ofDateAdjuster(w -> {
            LocalDate result = w;
            do {
                result = result.plusDays(1);
            } while (result.getDayOfWeek().getValue() >= 6);

            return  result;
        });

        final LocalDate nextWorkDay = today.with(nextWorkdayAdjuster);
        assertThat(nextWorkDay, is(equalTo(LocalDate.of(2018, 1, 22))));
    }

    @Test
    public void testFormatLocalDateTime() {
        final LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm");
        System.out.println(today.toString());
        System.out.println(formatter.format(today));
    }
}
