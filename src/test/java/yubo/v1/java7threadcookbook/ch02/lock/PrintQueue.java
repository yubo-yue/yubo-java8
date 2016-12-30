package yubo.v1.java7threadcookbook.ch02.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
    private final Lock queueLock = new ReentrantLock();

    public void printJob(final Object document) {
        queueLock.lock();

        try {
            long duration = (long) (Math.random() * 10000);
            System.out.println(Thread.currentThread().getName() + ":PrintQueue: Print a job during " + duration + " seconds");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }
    }

    public static void main(String[] args) {
        final PrintQueue printQueue = new PrintQueue();
        for (int i = 0; i < 10; i++) {
            new Thread(new Job(printQueue)).start();
        }
    }
}

class Job implements Runnable {
    private PrintQueue queue;

    public Job(final PrintQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
        queue.printJob(new Object());
        System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
    }
}
