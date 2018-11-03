package yubo.paumard.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Simple {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Runnable task = () -> {
            System.out.println("in thread " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task is running");
        };

        Future<?> future = service.submit(task);


        System.out.println("Thread " + Thread.currentThread().getName() + " doing other works");

        future.cancel(true);
        try {
            future.get(3, TimeUnit.SECONDS);
            System.out.println("in thread " + Thread.currentThread().getName() + ", task complete in 3 seconds");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("in thread " + Thread.currentThread().getName() + ", task didn't complete in 3 seconds");

        }
        service.shutdown();
    }
}
