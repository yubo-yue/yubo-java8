package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ThreadSafeWithDelayedElementCookbook {
    public static void main(String[] args) throws InterruptedException {
        final DelayQueue<Event> queue = new DelayQueue<>();
        final Thread[] threads = new Thread[5];

        for (int i = 0; i < threads.length; i++) {
            Task task = new Task(i + 1, queue);
            threads[i] = new Thread(task);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        do {
            int counter = 0;
            Event event;
            do {
                event = queue.poll();
                if (event != null) counter++;
            } while (event != null);
            System.out.printf("At %s you have read %d events\n", LocalDateTime.now(), counter);
            TimeUnit.MILLISECONDS.sleep(500);
        } while (queue.size() > 0);
    }

    @AllArgsConstructor
    private static class Event implements Delayed {
        @NonNull
        private LocalDateTime startDate;

        @Override
        public long getDelay(TimeUnit unit) {
            LocalDateTime now = LocalDateTime.now();
            return unit.convert(Duration.between(now, startDate).toMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
            if (result < 0) return -1;
            if (result > 0) return 1;
            return 0;
        }
    }

    @AllArgsConstructor
    private static class Task implements Runnable {
        private int id;
        private DelayQueue<Event> queue;

        @Override
        public void run() {
            final LocalDateTime now = LocalDateTime.now();
            final LocalDateTime delay = now.plus(1000 * id, ChronoUnit.MILLIS);

            for (int i = 0; i < 100; i++) {
                final Event event = new Event(delay);
                queue.add(event);
            }
        }
    }
}
