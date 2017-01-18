package yubo.v1.java7threadcookbook.ch07;

import java.time.Duration;
import java.time.LocalDateTime;

class MyThread extends Thread {
    private LocalDateTime creationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public MyThread(final Runnable target, final String name) {
        super(target, name);
        creationDate = LocalDateTime.now();
    }

    @Override
    public void run() {
        startDate = LocalDateTime.now();
        super.run();
        endDate = LocalDateTime.now();
    }

    public long getExecutionTimeInMillis() {
        return Duration.between(startDate, endDate).toMillis();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getName());
        buffer.append(": ");
        buffer.append(" Creation Date: ");
        buffer.append(creationDate);
        buffer.append(" : Running time: ");
        buffer.append(getExecutionTimeInMillis());
        buffer.append(" Milliseconds.");
        return buffer.toString();
    }
}
