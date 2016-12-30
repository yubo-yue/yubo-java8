package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class FactorialCaculator implements Callable<Integer> {
    private Integer number;

    public FactorialCaculator(final Integer integer) {
        this.number = integer;
    }

    @Override
    public Integer call() throws Exception {
        int result = 1;

        if (number == 0 || number == 1)
            return 1;
        for (int i = 2; i <= number; i++) {
            result = result * i;
            TimeUnit.MILLISECONDS.sleep(20);
        }
        System.out.printf("%s: %d%n", Thread.currentThread().getName(), result);
        return result;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        List<Future<Integer>> futures = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Integer number = random.nextInt(10);
            FactorialCaculator cal = new FactorialCaculator(number);

            Future<Integer> result = executor.submit(cal);
            futures.add(result);
        }

        do {
            System.out.printf("Number of completed tasks: %d%n", executor.getCompletedTaskCount());
            for (int i = 0; i < futures.size(); i++) {
                Future<Integer> f = futures.get(i);
                System.out.printf("Task: %d done? %s%n", i, f.isDone());
            }

            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (executor.getCompletedTaskCount() < futures.size());

        System.out.printf("Results\n");
        for (int i = 0; i < futures.size(); i++) {
            Future<Integer> result = futures.get(i);
            Integer number = null;
            try {
                number = result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            System.out.printf("Main: Task %d: %d%n", i, number);
        }
        executor.shutdown();
    }
}
