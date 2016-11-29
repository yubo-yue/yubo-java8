package yubo.v1.java7threadcookbook.daemon;

import java.time.Instant;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WriterTask implements Runnable {
    private Deque<Event> deque;

    public WriterTask(final Deque<Event> deque) {
        Objects.requireNonNull(deque);

        this.deque = deque;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            deque.addFirst(
                    Event.newInstance(Instant.now(),
                            String.format("The thread %s has generated an event", Thread.currentThread().getId())
                    )
            );

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
