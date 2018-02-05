package yubo.java8_impatient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebPageRetriever {

    private final ExecutorService executor;

    public WebPageRetriever() {
        executor = Executors.newFixedThreadPool(20);
    }

    public CompletionStage<String> getWebpage(final URL url) {
        final CompletableFuture<String> future = new CompletableFuture<>();

        Runnable task = () -> {
            try {
                    TimeUnit.SECONDS.sleep(30);
                String webPageString = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
                System.out.printf("current thread %s%n", Thread.currentThread().getName());
                future.complete(webPageString);
            } catch (IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }
        };

        executor.execute(task);
        return future;
    }

    public void stop() {
        executor.shutdownNow();
    }

    public static void main(String[] args) {
        try {
            final WebPageRetriever retriever = new WebPageRetriever();
            final CompletionStage<String> future = retriever.getWebpage(new URL("https://www.baidu.com"));

            System.out.printf("Main thread %s task submitted and future get%n", Thread.currentThread().getName());
            retriever.stop();

            future.thenAcceptAsync(result -> System.out.printf("result : %s%n", result));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
