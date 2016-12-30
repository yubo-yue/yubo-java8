package yubo.v1.java7threadcookbook.ch04.executorcreation;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectedTaskCookbook {
    public static void main(String[] args) {
        final RejectedTaskController rejectedTaskController = new RejectedTaskController();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.setRejectedExecutionHandler(rejectedTaskController);

        System.out.printf("Main: Starting.\n");
        for (int i = 0; i < 3; i++) {
            Task task = new Task("Task" + i);
            executor.submit(task);
        }

        System.out.printf("Main: Shutting down the Executor.\n");
        executor.shutdown();

        System.out.printf("Main: Sending another Task.\n");
        Task task = new Task("RejectedTask");
        executor.submit(task);

        System.out.printf("Main: End.%n");
    }

    private static class RejectedTaskController implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
            System.out.printf("RejectedTaskController: The task %s has been rejected%n", r);
            System.out.printf("RejectedTaskController: %s%n", executor.toString());
            System.out.printf("RejectedTaskController: Terminating: %s%n", executor.isTerminating());
            System.out.printf("RejectedTaskController: Terminated: %s%n", executor.isTerminated());
        }
    }

    private static class Task implements Runnable {
        private String name;

        public Task(final String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.printf("Task %s Starting%n", name);
            try {
                long duration = (long) (Math.random() * 10);
                System.out.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",
                        name, duration);
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %s: Ending%n", name);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("name", name)
                    .toString();
        }
    }
}
