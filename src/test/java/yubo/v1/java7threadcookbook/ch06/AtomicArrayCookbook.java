package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArrayCookbook {
    public static void main(String[] args) {
        final int THREADS = 100;
        final AtomicIntegerArray vector = new AtomicIntegerArray(1000);

        final Incrementer incrementer = new Incrementer(vector);
        final Decrementer decrementer = new Decrementer(vector);

        final Thread[] incrementers = new Thread[THREADS];
        final Thread[] decrementers = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            incrementers[i] = new Thread(incrementer);
            decrementers[i] = new Thread(decrementer);

            incrementers[i].start();
            decrementers[i].start();
        }

        for (int i = 0; i < THREADS; i++) {
            try {
                incrementers[i].join();
                decrementers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < vector.length(); i++) {
            if (vector.get(i) != 0)
                System.out.println("Vector[" + i + "] : " + vector.get(i));
        }

        System.out.println("Main: End of the example");
    }

    @AllArgsConstructor
    private static class Incrementer implements Runnable {
        private AtomicIntegerArray vector;

        @Override
        public void run() {
            for (int i = 0; i < vector.length(); i++) {
                vector.getAndIncrement(i);
            }
        }
    }

    @AllArgsConstructor
    private static class Decrementer implements Runnable {
        private AtomicIntegerArray vector;

        @Override
        public void run() {
            for (int i = 0; i < vector.length(); i++) {
                vector.getAndDecrement(i);
            }
        }
    }
}
