package yubo.paumard.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CompletableFutureWithSupplier {
    public static void main(String[] args) {
        final Supplier<String> supplier = () -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        };
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier);

        completableFuture.obtrudeValue("Too long");

        String result = completableFuture.join();

        System.out.println("Result = " + result);

        result = completableFuture.join();

        System.out.println("Result = " + result);
    }
}
