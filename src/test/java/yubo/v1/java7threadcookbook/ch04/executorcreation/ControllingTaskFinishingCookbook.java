package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.concurrent.*;

public class ControllingTaskFinishingCookbook {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        ResultTask[] tasks = new ResultTask[5];

        for (int i = 0; i < 5; i++) {
            ExecutableTask executableTask = new ExecutableTask(String.format("Task-%s", i));
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
                    System.out.printf("%s\n", tasks[i].get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
    }

    private static class ExecutableTask implements Callable<String> {
        private String name;

        public ExecutableTask(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String call() throws Exception {
            try {
                long duration = (long) (Math.random() * 10);
                System.out.printf("%s: Waiting %d seconds for results.%n", this.name, duration);
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
            }
            return String.format("Hello, world. I'm %s", name);
        }
    }

    private static class ResultTask extends FutureTask<String> {
        private final String name;


        public ResultTask(final Callable<String> callable) {
            super(callable);
            this.name = ((ExecutableTask) callable).getName();
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                System.out.printf("%s has been cancelled%n", name);
            } else {
                System.out.printf("%s has been completed%n", name);
            }
        }
    }


}
