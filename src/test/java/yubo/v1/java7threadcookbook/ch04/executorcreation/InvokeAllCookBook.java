package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class InvokeAllCookBook {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Task task = new Task(String.format("Task-%s", i));
            taskList.add(task);
        }

        List<Future<Result>> results = null;
        try {
            results = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        if (null == results) return;
        System.out.println("Main: Printing the results");
        for (int i = 0; i < results.size(); i++) {
            Future<Result> future = results.get(i);
            try {
                Result result = future.get();
                System.out.println(result.getName() + ": " + result.
                        getValue());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Result {
        private String name;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private static class Task implements Callable<Result> {
        private String name;

        public Task(final String name) {
            this.name = name;
        }

        @Override
        public Result call() throws Exception {
            System.out.printf("%s: Staring%n", this.name);
            try {
                long duration = (long) (Math.random() * 10);
                System.out.printf("%s: Waiting %d seconds for results.%n", this.name, duration);
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int value = 0;
            for (int i = 0; i < 5; i++) {
                value += (int) (Math.random() * 100);
            }
            final Result r = new Result();
            r.setName(name);
            r.setValue(value);
            return r;
        }
    }
}
