package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentLinkedDeque;

public class NonBlockingThreadSafeListCookbook {

    public static void main(String[] args) {
        final ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
        final Thread[] threads = new Thread[100];

        for (int i = 0; i < threads.length; i++) {
            final AddTask addTask = new AddTask(list);
            threads[i] = new Thread(addTask);
            threads[i].start();
        }

        System.out.printf("Main: %d AddTask threads have been launched%n", threads.length);

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Main: Size of the List: %d\n", list.size());

        for (int i = 0; i < threads.length; i++) {
            final PollTask pollTask = new PollTask(list);
            threads[i] = new Thread(pollTask);
            threads[i].start();
        }
        System.out.printf("Main: %d PollTask threads have been launched\n", threads.length);

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Main: Size of the List: %d\n", list.size());
    }

    @AllArgsConstructor
    private static class AddTask implements Runnable {
        private final ConcurrentLinkedDeque<String> list;


        @Override
        public void run() {
            final String name = Thread.currentThread().getName();

            for (int i = 0; i < 10000; i++) {
                list.add(String.format("%s:Element %d", name, i));
            }
        }
    }

    @AllArgsConstructor
    private static class PollTask implements Runnable {
        private final ConcurrentLinkedDeque<String> list;

        @Override
        public void run() {
            for (int i = 0; i < 5000; i++) {
                list.pollFirst();
                list.pollLast();
            }
        }
    }
}
