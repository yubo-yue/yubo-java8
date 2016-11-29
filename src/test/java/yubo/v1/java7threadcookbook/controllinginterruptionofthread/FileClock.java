package yubo.v1.java7threadcookbook.controllinginterruptionofthread;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class FileClock implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s\n", ZonedDateTime.now());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FileClock clock = new FileClock();
        new Thread(clock).start();
    }
}
