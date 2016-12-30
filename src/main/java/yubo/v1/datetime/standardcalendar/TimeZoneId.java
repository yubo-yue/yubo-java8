package yubo.v1.datetime.standardcalendar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TimeZoneId {

    public static void main(String[] args) {
        final Set<String> allZones = ZoneId.getAvailableZoneIds();
        List<String> zones = new ArrayList<>(allZones);

        Collections.sort(zones);

        LocalDateTime dt = LocalDateTime.now();
        Path p = Paths.get("/tmp/timeZones");
        try (BufferedWriter writer = Files.newBufferedWriter(p, StandardCharsets.US_ASCII)) {

            for (String s : zones) {
                ZoneId zoneId = ZoneId.of(s);
                ZonedDateTime zdt = dt.atZone(zoneId);
                ZoneOffset offset = zdt.getOffset();
                int secondsOfHour = offset.getTotalSeconds() / (60 * 60);
                String out = String.format("%35s %10s %n", zoneId, offset);

                if (secondsOfHour != 0) {
                    System.out.println(out);
                }

                writer.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
