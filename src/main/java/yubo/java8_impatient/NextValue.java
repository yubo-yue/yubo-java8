package yubo.java8_impatient;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NextValue {
    private static AtomicLong nextNumber = new AtomicLong(2);

    public static void main(String[] args) {
        Executor executor = Executors.newFixedThreadPool(2);
        for (long i = 0; i < 10; i++) {
            executor.execute(() -> System.out.printf("%s: number is %d%n", Thread.currentThread().getName(), nextNumber.accumulateAndGet(1, Math::addExact)));
        }
    }
}
