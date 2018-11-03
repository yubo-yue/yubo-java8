package yubo.paumard.async;

import lombok.extern.slf4j.Slf4j;
import yubo.paumard.async.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Tasks pipeline
 * <p>
 * get user ids -> fetch users -> display users
 */
@Slf4j
public class ExceptionExample {
    public static void main(String[] args) {
        final Supplier<List<Long>> supplier = () -> {
            try {
                SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            log.info("Get user ids is running...");
            throw new IllegalStateException("No Data");

            //return Arrays.asList(1L, 2L, 3L);
        };

        final Function<List<Long>, List<User>> fetchUsers = ids -> {
            try {
                SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("Fetch users is running...");

            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        final Consumer<List<User>> displayer = users -> {
            log.info("Display users is running...");
            users.forEach(u -> log.info("{}", u));
        };


        final CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplier);
        final CompletableFuture<List<Long>> exception = supply.handle((ids, e) -> {
            if (Objects.nonNull(e)) {
                log.error("OMG...", e);
                return Arrays.asList();
            }
            return ids;
        });
        final CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
        final CompletableFuture<Void> display = fetch.thenAccept(displayer);


//        supply.join();
        try {
            //give chance to let async task to run in common fork join pool.
            SECONDS.sleep(10);
        } catch (InterruptedException e) {
        }

        log.info("Supply : done = {}, exception = {}", supply.isDone(), supply.isCompletedExceptionally());
        log.info("Fetch : done = {}, exception = {}", fetch.isDone(), fetch.isCompletedExceptionally());
        log.info("display : done = {}, exception = {}", display.isDone(), display.isCompletedExceptionally());
    }
}
