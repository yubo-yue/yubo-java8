package yubo.v1.java7threadcookbook.finalization;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        final DataSourceLoader dsl = new DataSourceLoader();
        final Thread t1 = new Thread(dsl);

        final NetworkConnectionLoader ncl = new NetworkConnectionLoader();
        final Thread t2 = new Thread(ncl);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Configuration has bee loaded. " + LocalDate.now());
    }
}
