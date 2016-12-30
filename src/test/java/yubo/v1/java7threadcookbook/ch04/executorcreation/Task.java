package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {
    private LocalDate initDate;
    private String name;

    public Task(final String name) {
        Objects.nonNull(name);
        this.name = name;
        this.initDate = LocalDate.now();
    }


    @Override
    public void run() {
        System.out.printf("%s: Task %s created on %s%n", Thread.currentThread().getName(), name, initDate);
        System.out.printf("%s: Task %s started on %s%n", Thread.currentThread().getName(), name, LocalDate.now());
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s: Task %s doing a task during %d seconds%n", Thread.currentThread().getName(),
                    name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s: Task %s completed on %s%n", Thread.currentThread().getName(), name, LocalDate.now());
    }
}
