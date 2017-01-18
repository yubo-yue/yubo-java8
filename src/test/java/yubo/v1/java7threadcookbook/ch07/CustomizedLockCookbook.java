package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CustomizedLockCookbook {
    private static class MyAbstractQueueSynchronizer extends AbstractQueuedSynchronizer {
        private AtomicInteger state;

        public MyAbstractQueueSynchronizer() {
            state = new AtomicInteger(0);
        }

        @Override
        protected boolean tryAcquire(int arg) {
            return state.compareAndSet(0, 1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return state.compareAndSet(1, 0);
        }
    }

    private static class MyLock implements Lock {
        private final AbstractQueuedSynchronizer synchronizer;

        public MyLock() {
            synchronizer = new MyAbstractQueueSynchronizer();
        }

        @Override
        public void lock() {
            synchronizer.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            synchronizer.acquireInterruptibly(1);
        }

        @Override
        public boolean tryLock() {
            try {
                return synchronizer.tryAcquireNanos(1, 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return synchronizer.tryAcquireNanos(1, TimeUnit.NANOSECONDS.convert(time, unit));
        }

        @Override
        public void unlock() {
            synchronizer.release(1);
        }

        @Override
        public Condition newCondition() {
            return synchronizer.new ConditionObject();
        }
    }

    @AllArgsConstructor
    private static class Task implements Runnable {
        private MyLock lock;

        private String name;

        @Override
        public void run() {
            lock.lock();

            System.out.printf("Task %s take the lock%n", name);
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.printf("Task %s free the lock%n", name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final MyLock lock = new MyLock();

        for (int i = 0; i < 10; i++) {
            final Task task = new Task(lock, String.format("Task-%d", i));
            Thread t = new Thread(task);
            t.start();
        }

        boolean value;
        do {
            try {
                value = lock.tryLock(1, TimeUnit.SECONDS);
                if (!value) {
                    System.out.printf("Trying to get lock%n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                value = false;
            }
        } while (!value);
        System.out.printf("Got the lock%n");
        lock.unlock();
        System.out.printf("Main: End %n");
    }
}
