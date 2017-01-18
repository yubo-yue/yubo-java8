package yubo.v1.java7threadcookbook.ch06;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateConcurrentRandomCookbook {
    public static void main(String[] args) {
        final Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final TaskLocalRandom task = new TaskLocalRandom();
            threads[i] = new Thread(task);
            threads[i].start();
        }
    }

    private static class TaskLocalRandom implements Runnable {
        public TaskLocalRandom() {
            ThreadLocalRandom.current();
        }

        @Override
        public void run() {
            final String name = Thread.currentThread().getName();
            for (int i = 0; i < 10; i++) {
                System.out.printf("%s: %d%n", name, ThreadLocalRandom.current().nextInt(10));
            }
        }
    }
}
