package yubo.v1.java7threadcookbook.finalization;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class NetworkConnectionLoader implements Runnable {
    @Override
    public void run() {
        System.out.println("Begnning connection loading: " + LocalDate.now());
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Connection loading finished: " + LocalDate.now());
    }
}
