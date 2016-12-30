package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeriodicallyScheduleCookBook {

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        System.out.printf("Main: Starting at: %s%n", LocalDateTime.now());

        Task task = new Task("Periodical Task");

        ScheduledFuture<?> future = service.scheduleAtFixedRate(task, 2, 4, TimeUnit.SECONDS);
        for (int i = 0; i < 100; i++) {
            System.out.printf("Delay %d%n", future.getDelay(TimeUnit.MILLISECONDS));
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        service.shutdown();
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: Finished at: %s\n", LocalDateTime.now());
    }

    private static class Task implements Runnable {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.printf("%s: Starting at : %s%n", name, LocalDateTime.now());
        }
    }
}
