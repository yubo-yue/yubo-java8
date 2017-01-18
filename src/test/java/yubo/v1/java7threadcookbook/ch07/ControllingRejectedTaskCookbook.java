package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ControllingRejectedTaskCookbook {
    public static void main(String[] args) {
        final RejectedTaskController rejectedTaskController = new RejectedTaskController();
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        executor.setRejectedExecutionHandler(rejectedTaskController);
        for (int i = 0; i < 3; i++) {
            final Task task = new Task(String.format("Task-%d", i));
            executor.submit(task);
        }

        System.out.printf("Main: shutdown the executor service%n");
        executor.shutdown();
        System.out.printf("Main: sending another task%n");
        final Task task = new Task("Another Task");
        executor.submit(task);
        System.out.println("Main: End");
    }

    private static class RejectedTaskController implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.printf("RejectedTaskController: task %s is rejected%n", r.toString());
            System.out.printf("RejectedTaskController: %s%n", executor.toString());
            System.out.printf("RejectedTaskController: %s%n", executor.isTerminating());
            System.out.printf("RejectedTaskController: %s%n", executor.isTerminated());
        }
    }

    @AllArgsConstructor
    private static class Task implements Runnable {
        private String name;

        @Override
        public void run() {
            System.out.printf("Task %s starting%n", name);
            long duration = (long) (Math.random() * 10);
            System.out.printf("Task %s running %d seconds%n", name, duration);
            try {
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %s ending%n", name);
        }
    }
}
