package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class OverrideTaskFinishingCookbook {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        ResultTask[] tasks = new ResultTask[5];
        for (int i = 0; i < 5; i++) {
            ExecutableTask executableTask = new ExecutableTask(String.format("Task-%d", i));
            tasks[i] = new ResultTask(executableTask);
            service.submit(tasks[i]);
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        for (int i = 0; i < tasks.length; i++) {
            tasks[i].cancel(true);
        }

        for (int i = 0; i < tasks.length; i++) {
            try {
                if (!tasks[i].isCancelled()) {
                    System.out.printf("%s%n", tasks[i].get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
    }

    @AllArgsConstructor
    private static class ExecutableTask implements Callable<String> {
        @Getter
        private String name;

        @Override
        public String call() throws Exception {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s waiting for %d seconds to complete %n", name, duration);
            TimeUnit.SECONDS.sleep(duration);

            return String.format("Hello, i am %s", name);
        }
    }

    private static class ResultTask extends FutureTask<String> {
        private String name;

        public ResultTask(Callable<String> callable) {
            super(callable);
            this.name = ((ExecutableTask) callable).getName();
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                System.out.printf("task %s is cancelled%n", name);
            } else {
                System.out.printf("task %s is completed %n", name);
            }
        }
    }
}
