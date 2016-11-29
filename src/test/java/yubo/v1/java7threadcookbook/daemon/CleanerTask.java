package yubo.v1.java7threadcookbook.daemon;

import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.Objects;

public class CleanerTask extends Thread {
    private Deque<Event> deque;

    public CleanerTask(final Deque<Event> deque) {
        Objects.requireNonNull(deque);

        this.deque = deque;

        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            clean(Instant.now());
        }
    }

    private void clean(Instant instant) {
        if (deque.isEmpty()) return;

        boolean deletedFlag = false;
        long difference;

        do {
            final Event event = deque.getLast();
            difference = Duration.between(event.getDate(), instant).toMillis();
            if (difference > 10000) {
                System.out.printf("Cleaner: %s\n", event.getEvent());
                deque.removeLast();
                deletedFlag = true;
            }
        } while (difference > 10000);

        if (deletedFlag) {
            System.out.printf("Cleaner: Size of the queue: %d\n", deque.
                    size());
        }

    }
}
