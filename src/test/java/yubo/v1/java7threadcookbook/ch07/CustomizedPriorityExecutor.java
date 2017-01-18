package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomizedPriorityExecutor {
    public static void main(String[] args) {
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<>());

        for (int i = 0; i < 4; i++) {
            final MyPriorityTask task = new MyPriorityTask(i, String.format("Task-%d", i));
            executor.execute(task);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 4; i < 8; i++) {
            final MyPriorityTask task = new MyPriorityTask(i, String.format("Task-%d", i));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: End of the program.\n");
    }

    @AllArgsConstructor
    private static class MyPriorityTask implements Runnable, Comparable<MyPriorityTask> {
        @Getter
        private final int priority;
        private final String name;

        @Override
        public void run() {
            System.out.printf("MyPriorityTask: %s Priority : %d\n", name, priority);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int compareTo(MyPriorityTask o) {
            if (priority < o.priority)
                return 1;
            if (priority > o.priority)
                return -1;
            return 0;
        }
    }

    protected static <T> RunnableFuture<T> newTaskForValue(Runnable runnable, T value) {
        return new ComparableFutureTask<>(runnable, value);
    }

    private static class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask<T>> {
        private Object object;

        public ComparableFutureTask(Runnable r, T result) {
            super(r, result);
            object = r;
        }

        public int compareTo(ComparableFutureTask<T> o) {
            if (this == o) {
                return 0;
            }
            if (o == null) {
                return -1; // this has higher priority than null
            }
            if (object != null && o.object != null) {
                if (object.getClass().equals(o.object.getClass())) {
                    if (object instanceof Comparable) {
                        return ((Comparable) object).compareTo(o.object);
                    }
                }
            }
            return 0;
        }
    }
}
