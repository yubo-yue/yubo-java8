package yubo.v1.java7threadcookbook.ch07;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CancelTaskCookbook {
    public static void main(String[] args) {
        final ExecutorService service = Executors.newCachedThreadPool();
        Future<String> result = service.submit(new Task());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: cancel the task%n");
        result.cancel(true);
        System.out.printf("Main: cancelled %s%n", result.isCancelled());
        System.out.printf("Main: done %s%n", result.isDone());
        service.shutdown();
        System.out.printf("Main: completed%n");

    }

    private static class Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            while (true) {
                System.out.printf("Task: Test%n");
                TimeUnit.SECONDS.sleep(20);
            }
        }
    }
}
