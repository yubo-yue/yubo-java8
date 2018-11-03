package yubo.paumard.async;

import lombok.extern.slf4j.Slf4j;
import yubo.paumard.async.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class AsyncExample {
    public static void main(String[] args) {

        final ExecutorService executor = Executors.newFixedThreadPool(2);
        final Supplier<List<Long>> supplier = () -> {
            try {
                SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            return Arrays.asList(1L, 2L, 3L);
        };

        final Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            try {
                SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Supplier<List<User>> userSupplier = () -> {
                log.info("Now fetching users....");
                return ids.stream().map(User::new).collect(Collectors.toList());
            };
            return CompletableFuture.supplyAsync(userSupplier);
        };

        final Consumer<List<User>> displayer = users -> {
            log.info("Now displaying...");
            users.forEach(System.out::println);
        };

        final CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplier);

        completableFuture.thenCompose(fetchUsers)
                .thenAcceptAsync(displayer, executor);

        try {
            SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
