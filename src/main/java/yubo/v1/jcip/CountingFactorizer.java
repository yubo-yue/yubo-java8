package yubo.v1.jcip;

import java.util.concurrent.atomic.AtomicLong;

public class CountingFactorizer {
    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }

    public void service() {
        count.incrementAndGet();
    }
}
