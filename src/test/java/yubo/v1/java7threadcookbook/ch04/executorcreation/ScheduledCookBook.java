package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledCookBook {

    public static void main(String[] args) {
        final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        System.out.printf("Start at %s%n", LocalDateTime.now());
        for (int i = 0; i < 5; i++) {
            final Task task = new Task(String.format("Task-%s", i));
            service.schedule(task, i * 2, TimeUnit.SECONDS);
        }

        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: Ends at: %s\n", LocalDateTime.now());
    }

    private static class Task implements Callable<String> {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            System.out.printf("%s: Starting at : %s%n", name, LocalDateTime.now());
            return "Hello World";
        }
    }
}
