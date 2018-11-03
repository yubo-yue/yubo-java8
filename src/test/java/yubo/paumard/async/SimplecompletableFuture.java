package yubo.paumard.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SimplecompletableFuture {
    public static void main(String[] args) {
        CompletableFuture<Void> cf = new CompletableFuture<>();

        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            System.out.println("In " + Thread.currentThread().getName() + " stop wait....");
            cf.complete(null);
        };

        CompletableFuture.runAsync(task);

        Void nil = cf.join();
        System.out.println("we are done");
    }
}
