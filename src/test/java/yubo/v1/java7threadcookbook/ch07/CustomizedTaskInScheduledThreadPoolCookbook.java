package yubo.v1.java7threadcookbook.ch07;

import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomizedTaskInScheduledThreadPoolCookbook {
    public static void main(String[] args) {
        final MyScheduledThreadpoolExecutor executor = new MyScheduledThreadpoolExecutor(2);
        Task task = new Task();
        System.out.printf("Main: %s\n", LocalDateTime.now());
        executor.schedule(task, 1, TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task = new Task();
        System.out.printf("Main: %s\n", LocalDateTime.now());
        executor.scheduleAtFixedRate(task, 1, 3, TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: End of the program.\n");
    }

    private static class MyScheduledTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
        private RunnableScheduledFuture<V> task;
        private ScheduledThreadPoolExecutor executor;
        @Setter
        private long period;
        private long startDate;

        public MyScheduledTask(final Runnable runnable, V result, final RunnableScheduledFuture<V> task,
                               final ScheduledThreadPoolExecutor executor) {
            super(runnable, result);
            this.task = task;
            this.executor = executor;
        }

        @Override
        public boolean isPeriodic() {
            return this.task.isPeriodic();
        }

        @Override
        public long getDelay(final TimeUnit unit) {
            if (!this.task.isPeriodic()) {
                return task.getDelay(unit);
            } else {
                if (startDate == 0) {
                    return task.getDelay(unit);
                } else {
                    LocalDateTime now = LocalDateTime.now();
                    long delay = startDate - now.toEpochSecond(ZoneOffset.UTC);
                    return unit.convert(delay, TimeUnit.SECONDS);
                }
            }
        }

        @Override
        public int compareTo(Delayed o) {
            return task.compareTo(o);
        }

        @Override
        public void run() {
            if (isPeriodic() && (!executor.isShutdown())) {
                startDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + period;
                executor.getQueue().add(this);
            }

            System.out.printf("Pre-MyScheduledTask: %s\n", LocalDateTime.now());
            System.out.printf("MyScheduledTask: Is Periodic: %s\n", isPeriodic());
            super.runAndReset();
            System.out.printf("Post-MyScheduledTask: %s\n", LocalDateTime.now());
        }
    }

    private static class MyScheduledThreadpoolExecutor extends ScheduledThreadPoolExecutor {

        public MyScheduledThreadpoolExecutor(int corePoolSize) {
            super(corePoolSize);
        }

        @Override
        protected <V> RunnableScheduledFuture<V> decorateTask(final Runnable runnable,
                                                              final RunnableScheduledFuture<V> task) {
            return new MyScheduledTask<>(runnable, null, task, this);
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, long initialDelay,
                                                      long period, final TimeUnit unit) {
            ScheduledFuture<?> task = super.scheduleAtFixedRate(command, initialDelay, period, unit);
            final MyScheduledTask<?> myTask = (MyScheduledTask<?>) task;
            myTask.setPeriod(TimeUnit.SECONDS.convert(period, unit));
            return task;
        }
    }

    private static class Task implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
