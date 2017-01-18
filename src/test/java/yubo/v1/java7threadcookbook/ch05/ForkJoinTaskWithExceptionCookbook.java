package yubo.v1.java7threadcookbook.ch05;

import lombok.AllArgsConstructor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinTaskWithExceptionCookbook {
    public static void main(String[] args) {
        int array[] = new int[100];
        Task task = new Task(array, 0, 100);
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(task);
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (task.isCompletedAbnormally()) {
            System.out.printf("Main: %s\n", task.getException());
        }
        System.out.printf("Main: Result: %d", task.join());
    }


    @AllArgsConstructor
    private static class Task extends RecursiveTask<Integer> {
        private int[] array;
        private int start, end;

        @Override
        protected Integer compute() {
            System.out.printf("Task: Start from %d to %d\n", start, end);
            if (end - start < 10) {
                if ((3 > start) && (3 < end)) {
                    completeExceptionally(new RuntimeException("This task throws an Exception: Task from  " + start + " to " + end));
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                int mid = (end + start) / 2;
                final Task task1 = new Task(array, start, mid);
                final Task task2 = new Task(array, mid, end);
                invokeAll(task1, task2);
            }
            System.out.printf("Task: End form %d to %d\n", start, end);
            return 0;
        }
    }
}
