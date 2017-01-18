package yubo.v1.java7threadcookbook.ch07;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomizedThreadPoolExecutor {
    public static void main(String[] args) {
        MyExecutor myExecutor = new MyExecutor(2, 4, 1000, TimeUnit.
                MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        List<Future<String>> results = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            SleepTwoSecondsTask task = new SleepTwoSecondsTask();
            Future<String> result = myExecutor.submit(task);
            results.add(result);
        }

        for (int i = 0; i < 5; i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: Result for Task %d : %s\n", i, result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        myExecutor.shutdown();

        for (int i = 5; i < 10; i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: Result for Task %d : %s\n", i, result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        try {
            myExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: End of the program.\n");
    }

    private static class MyExecutor extends ThreadPoolExecutor {
        private final ConcurrentHashMap<String, LocalDateTime> startTimes;

        public MyExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                          TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            startTimes = new ConcurrentHashMap<>();
        }

        @Override
        public void shutdown() {
            System.out.printf("MyExecutor: is going to shutdown%n");
            System.out.printf("MyExecutor: Executed Task: %d%n", getCompletedTaskCount());
            System.out.printf("MyExecutor: Running Task: %d%n", getActiveCount());
            System.out.printf("MyExecutor: Pending Task: %d%n", getQueue().size());

            super.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            System.out.printf("MyExecutor: is going to shutdown immediately%n");
            System.out.printf("MyExecutor: Executed Task: %d%n", getCompletedTaskCount());
            System.out.printf("MyExecutor: Running Task: %d%n", getActiveCount());
            System.out.printf("MyExecutor: Pending Task: %d%n", getQueue().size());

            return super.shutdownNow();
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.printf("MyExecutor: A task is begining: %s, %s%n", t.getName(), r.hashCode());
            startTimes.put(String.valueOf(r.hashCode()), LocalDateTime.now());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            Future<?> result = (Future<?>) r;
            try {
                System.out.printf("*********************************\n");
                System.out.printf("MyExecutor: A task is finishing.\n");
                System.out.printf("MyExecutor: Result: %s\n", result.get());
                LocalDateTime startDate = startTimes.remove(String.valueOf(r.
                        hashCode()));
                LocalDateTime finishDate = LocalDateTime.now();
                long diff = Duration.between(startDate, finishDate).get(ChronoUnit.SECONDS);
                System.out.printf("MyExecutor: Duration: %d\n", diff);
                System.out.printf("*********************************\n");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static class SleepTwoSecondsTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(2);
            return LocalDateTime.now().toString();
        }
    }

}
