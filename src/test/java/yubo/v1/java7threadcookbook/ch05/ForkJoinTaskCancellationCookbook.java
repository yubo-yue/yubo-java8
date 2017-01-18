package yubo.v1.java7threadcookbook.ch05;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinTaskCancellationCookbook {
    public static void main(String[] args) {
        final ArrayGenerator generator = new ArrayGenerator();
        int array[] = generator.generateArray(1000);
        final TaskManager manager = new TaskManager();
        final ForkJoinPool pool = new ForkJoinPool();
        final Task task = new Task(array, 0, 1000, 5, manager);
        pool.execute(task);
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: The program has finished\n");

    }

    private static class ArrayGenerator {
        public int[] generateArray(int size) {
            final int array[] = new int[size];
            final Random random = new Random();
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(10);
            }
            return array;
        }
    }

    private static class TaskManager {
        private final List<ForkJoinTask<Integer>> tasks = new ArrayList<>();

        public void addTask(final ForkJoinTask<Integer> task) {
            this.tasks.add(task);
        }

        public void cancelTasks(final ForkJoinTask<Integer> taskToCancel) {
            for (ForkJoinTask t : tasks) {
                if (t != taskToCancel) {
                    t.cancel(true);
                    ((Task) t).writeCancelMessage();
                }
            }
        }
    }

    @AllArgsConstructor
    private static class Task extends RecursiveTask<Integer> {
        private final static int NOT_FOUND = -1;
        private final int numbers[];
        private final int start, end;
        private final int number;
        private final TaskManager manager;

        @Override
        protected Integer compute() {
            System.out.println("Task: " + start + ":" + end);
            int ret;

            if (end - start > 10) {
                ret = launchTasks();
            } else {
                ret = lookForNumber();
            }

            return ret;
        }

        private int lookForNumber() {
            for (int i = start; i < end; i++) {
                if (number == numbers[i]) {
                    System.out.printf("Task: Number %d found in position %d\n", number, i);
                    manager.cancelTasks(this);
                    return i;
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return NOT_FOUND;
        }

        private int launchTasks() {
            int mid = (start + end) / 2;
            Task task1 = new Task(numbers, start, mid, number, manager);
            Task task2 = new Task(numbers, mid, end, number, manager);
            manager.addTask(task1);
            manager.addTask(task2);
            task1.fork();
            task2.fork();

            int returnValue;
            returnValue = task1.join();
            if (returnValue != -1) {
                return returnValue;
            }
            returnValue = task2.join();
            return returnValue;
        }

        public void writeCancelMessage() {
            System.out.printf("Task: Canceled task from %d to %d%n", start, end);
        }
    }
}
