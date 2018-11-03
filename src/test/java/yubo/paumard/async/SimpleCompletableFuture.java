package yubo.paumard.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SimpleCompletableFuture {
    public static void main(String[] args) {
        final Runnable task = () -> System.out.println("Hello World in " + Thread.currentThread().getName());

        final ExecutorService service = Executors.newSingleThreadExecutor();

        final Future<?> future = service.submit(task);

        final CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(task, service);

        final Supplier<String> supplyTask = () -> {
            System.out.println("Hello supplier in " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Hello Supplier " + Thread.currentThread().getName();
        };

        final CompletableFuture<String> supplyTaskFuture = CompletableFuture.supplyAsync(supplyTask, service);

        supplyTaskFuture.complete("Too Long!");

        String result = supplyTaskFuture.join();

        System.out.println("Result is " + result);

        service.shutdown();
    }
}
