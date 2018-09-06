package yubo.paumard.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstCompletableFuture {
    public static void main(String[] args) {
        final Runnable task = () -> {
            System.out.println("i am running asynchronously in thread " + Thread.currentThread().getName());
        };
        final ExecutorService executorService = Executors.newFixedThreadPool(1);

        CompletableFuture.runAsync(task, executorService);

        executorService.shutdown();
    }
}
