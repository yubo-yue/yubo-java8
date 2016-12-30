package yubo.v1.java7threadcookbook.ch02.readwritelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PricesInfo {
    private double price1;
    private double price2;

    private ReadWriteLock readWriteLock;

    public PricesInfo() {
        this.price1 = 1.0;
        this.price2 = 1.0;
        readWriteLock = new ReentrantReadWriteLock(true);
    }

    public double getPrice1() {
        readWriteLock.readLock().lock();
        double value = price1;
        readWriteLock.readLock().unlock();

        return value;
    }

    public double getPrice2() {
        readWriteLock.readLock().lock();
        double value = price2;
        readWriteLock.readLock().unlock();
        return value;
    }

    public void setPrices(double price1, double price2) {
        readWriteLock.writeLock().lock();
        this.price1 = price1;
        this.price2 = price2;
        readWriteLock.writeLock().unlock();
    }

    public static void main(String[] args) {
        PricesInfo pricesInfo = new PricesInfo();
        Thread[] readers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readers[i] = new Thread(new Reader(pricesInfo));
        }

        Thread writer = new Thread(new Writer(pricesInfo));

        for (int i = 0; i < 5; i++) {
            readers[i].start();
        }
        writer.start();
    }
}

class Reader implements Runnable {
    private PricesInfo pricesInfo;

    public Reader(final PricesInfo pricesInfo) {
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s: Price 1: %f\n", Thread.
                    currentThread().getName(), pricesInfo.getPrice1());
            System.out.printf("%s: Price 2: %f\n", Thread.
                    currentThread().getName(), pricesInfo.getPrice2());
        }
    }
}

class Writer implements Runnable {
    private PricesInfo pricesInfo;

    public Writer(final PricesInfo pricesInfo) {
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.printf("Writer: Attempt to modify the prices.\n");
            pricesInfo.setPrices(Math.random() * 10, Math.random() * 8);
            System.out.printf("Writer: Prices have been modified.\n");
            try {
                TimeUnit.MILLISECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
