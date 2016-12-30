package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.concurrent.*;

public class CancelTaskCookbook {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Task task = new Task();
        System.out.printf("Executing task%n");

        Future<String> result = executor.submit(task);


        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Cancel task%n");
        result.cancel(true);
        System.out.printf("Task cancelled %s%n", result.isCancelled());
        System.out.printf("Task done %s%n", result.isDone());
        executor.shutdown();
        System.out.printf("The executor completed %n");
    }

    private static class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            while (true) {
                System.out.printf("Task: test %n");
                TimeUnit.SECONDS.sleep(5);
            }
        }
    }

}
