package yubo.v1.java7threadcookbook.threadfactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MyThreadFactory implements ThreadFactory {
    private int counter;

    private String prefix;

    private List<String> stats;

    public MyThreadFactory(final String prefix) {
        Objects.nonNull(prefix);

        this.counter = 0;
        this.prefix = prefix;
        this.stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = new Thread(r, prefix + "-Thread-" + counter);
        counter++;
        stats.add(String.format("Created thread %d with name %s on %s\n", t.getId(), t.getName(), LocalDate.now()));
        return t;
    }

    public String getStats() {
        return stats.stream().collect(Collectors.joining());
    }

    public static void main(String[] args) {
        final MyThreadFactory factory = new MyThreadFactory("yubo");

        for (int i = 0; i < 10; i++) {
            factory.newThread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.printf("%s\n", factory.stats);
    }
}
