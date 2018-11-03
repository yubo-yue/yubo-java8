package yubo.paumard.async;

import com.sun.tools.javac.util.List;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class SimpleChainTask {
    public static void main(String[] args) {
        CompletableFuture<Void> cf = CompletableFuture
                .supplyAsync(() -> {
                    log.info("supply use ids");
                    return List.of(1, 2, 3);})
                .thenRunAsync(() -> log.info("Task done. Daemon = {}", Thread.currentThread().isDaemon()));

        cf.join();
        log.info("Deamon = {}", Thread.currentThread().isDaemon());
    }
}
