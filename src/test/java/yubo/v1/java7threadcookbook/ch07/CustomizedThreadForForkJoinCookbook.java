package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class CustomizedThreadForForkJoinCookbook {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        final MyWorkerThreadFactory factory = new MyWorkerThreadFactory();
        ForkJoinPool pool = new ForkJoinPool(2, factory, null, false);
        int[] array = new int[100000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        MyRecursiveTask task = new MyRecursiveTask(array, 0, array.length);
        pool.execute(task);
        task.join();
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
        System.out.printf("Main: result %d%n", task.get());
        System.out.println("Main: end.");
    }

    private static class MyWorkerThread extends ForkJoinWorkerThread {
        private static ThreadLocal<Integer> taskCounter = new ThreadLocal<>();

        protected MyWorkerThread(ForkJoinPool pool) {
            super(pool);
        }

        @Override
        protected void onStart() {
            super.onStart();
            System.out.printf("MyWorkerThread %d: initializing task counter %n", getId());
            taskCounter.set(0);
        }

        @Override
        protected void onTermination(Throwable exception) {
            System.out.printf("MyWorkerThread %d:%d%n", getId(), taskCounter.get());
            super.onTermination(exception);
        }

        public void addTask() {
            int counter = taskCounter.get().intValue();
            counter++;
            taskCounter.set(counter);
        }
    }

    private static class MyWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            return new MyWorkerThread(pool);
        }
    }

    @AllArgsConstructor
    private static class MyRecursiveTask extends RecursiveTask<Integer> {
        private int[] array;
        private int start, end;

        @Override
        protected Integer compute() {
            Integer ret;
            MyWorkerThread thread = (MyWorkerThread) Thread.currentThread();
            thread.addTask();
            if (end - start < 100) {
                ret = array[start];
            } else {
                int mid = (start + end) / 2;
                MyRecursiveTask task1 = new MyRecursiveTask(array, start, mid);
                MyRecursiveTask task2 = new MyRecursiveTask(array, mid, end);
                invokeAll(task1, task2);

                ret = addResults(task1, task2);
            }

            return ret;
        }

        private Integer addResults(MyRecursiveTask task1, MyRecursiveTask task2) {
            int val = 0;
            try {
                val = task1.get() + task2.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return val;
        }
    }

}
