package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private final ThreadPoolExecutor executor;

    public Server() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    public void executeTask(final Task task) {
        System.out.printf("Server: a new task arrived%n");
        executor.execute(task);
        System.out.printf("Server: Pool Size = %d%n", executor.getPoolSize());
        System.out.printf("Server: Active Count = %d%n", executor.getActiveCount());
        System.out.printf("Server: Task Count = %d%n", executor.getTaskCount());
        System.out.printf("Server: Completed Tasks = %d%n", executor.getCompletedTaskCount());
    }

    public void endServer() {
        try {
            executor.shutdown();
            boolean succeed = executor.awaitTermination(30, TimeUnit.SECONDS);
            if (!succeed)
                System.out.printf("Server: the server don't terminate in %d seconds%n", 30);
        } catch (InterruptedException e) {
            List<Runnable> neverCommencedTasks = executor.shutdownNow();
            System.out.printf("Server: Before shutdown, there are %d tasks are never executed%n", neverCommencedTasks.size());
        }
    }

    public static void main(String[] args) {
        final Server server = new Server();

        for (int i = 0; i < 100; i++) {
            final Task task = new Task(String.format("Task-%d", i));
            server.executeTask(task);
        }

        server.endServer();
    }
}
