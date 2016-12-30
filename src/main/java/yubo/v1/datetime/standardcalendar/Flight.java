package yubo.v1.datetime.standardcalendar;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
        LocalDateTime leaveTime = LocalDateTime.of(2016, Month.DECEMBER, 30, 16, 30);
        final ZoneId leavingZone = ZoneId.of("America/Los_Angeles");
        ZonedDateTime departure = ZonedDateTime.of(leaveTime, leavingZone);

        String out1 = departure.format(dtf);
        System.out.printf("LEAVING: %s (%s)%n", out1, leavingZone);

        final ZoneId arrivalZone = ZoneId.of("Asia/Tokyo");
        ZonedDateTime arrival = departure.withZoneSameInstant(arrivalZone).plusMinutes(650);

        String out2 = arrival.format(dtf);
        System.out.printf("ARRIVING: %s (%s)%n", out2, arrivalZone);
    }
}
