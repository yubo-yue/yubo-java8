package yubo.v1.java7threadcookbook.ch07;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorsWithCustomizedThreadFactoryCookbook {
    public static void main(String[] args) {
        final MyThreadFactory factory = new MyThreadFactory("mythread");
        final ExecutorService service = Executors.newCachedThreadPool(factory);

        final MyTask task = new MyTask();
        service.submit(task);

        service.shutdown();
        try {
            service.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
        }

        System.out.println("End of Main application.");
    }
}
