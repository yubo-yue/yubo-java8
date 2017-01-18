package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.PriorityBlockingQueue;

public class BlockingThreadSafeListOrderedCookbook {
    public static void main(String[] args) {
        final PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>(3);
        final Thread[] taskThreads = new Thread[5];
        for (int i = 0; i < taskThreads.length; i++) {
            final Task task = new Task(i, queue);
            taskThreads[i] = new Thread(task);
        }
        for (int i = 0; i < taskThreads.length; i++) {
            taskThreads[i].start();
        }
        for (int i = 0; i < taskThreads.length; i++) {
            try {
                taskThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Main: Queue Size: %d\n", queue.size());

        for (int i = 0; i < taskThreads.length * 100; i++) {
            final Event event = queue.poll();
            System.out.printf("Thread %s: Priority %d%n", event.getThread(), event.getPriority());
        }
        System.out.printf("Main: Queue Size: %d\n", queue.size());
        System.out.printf("Main: End of the program\n");
    }

    @AllArgsConstructor
    private static class Event implements Comparable<Event> {
        @NonNull
        @Getter
        private final int thread;
        @NonNull
        @Getter
        private final int priority;


        @Override
        public int compareTo(final Event o) {
            if (priority > o.priority)
                return -1;
            if (priority < o.priority)
                return 1;
            return 0;
        }
    }

    @AllArgsConstructor
    private static class Task implements Runnable {
        @NonNull
        @Getter
        private final int id;
        @NonNull
        private final PriorityBlockingQueue<Event> queue;

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                final Event event = new Event(id, i);
                queue.add(event);
            }
        }
    }
}
