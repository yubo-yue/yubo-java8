package yubo.v1.java7threadcookbook.finalization;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class DataSourceLoader implements Runnable {
    @Override
    public void run() {
        System.out.println("Beginning data source loading: " + LocalDate.now());
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Data source loading has finished: " + LocalDate.now());

    }
}
