package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class CustomizeTaskInForkJoinCookbook {

    @AllArgsConstructor
    private static abstract class MyWorkTask extends ForkJoinTask<Void> {
        @Getter
        private String name;


        @Override
        public Void getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Void value) {
            //
        }

        @Override
        protected boolean exec() {
            Instant start = Instant.now();
            compute();
            Instant end = Instant.now();
            long millis = Duration.between(start, end).toMillis();
            System.out.printf("MyWorkTask: %d milliseconds%n", millis);
            return true;
        }

        protected abstract void compute();
    }

    private static class Task extends MyWorkTask {
        private int[] array;
        private int start, end;

        public Task(final String name, int[] array, int start, int end) {
            super(name);
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start > 100) {
                int mid = (end + start) / 2;
                Task task1 = new Task(this.getName() + "1", array, start, mid);
                Task task2 = new Task(this.getName() + "2", array, mid, end);
                invokeAll(task1, task2);
            } else {
                for (int i = start; i < end; i++) {
                    array[i]++;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        int array[] = new int[10000];
        ForkJoinPool pool = new ForkJoinPool(2);
        Task task = new Task("Task", array, 0, array.length);
        pool.invoke(task);
        pool.shutdown();
        System.out.printf("Main: End of the program.\n");
    }
}
