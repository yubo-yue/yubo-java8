package yubo.v1.java7threadcookbook.ch02.condition;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class EventStorage {
    private int maxSize;
    private List<LocalDate> storage;

    public EventStorage() {
        this.maxSize = 10;
        this.storage = new LinkedList<>();
    }

    public synchronized void get() {
        while (storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Get: %d: %s\n", storage.
                size(), ((LinkedList<?>) storage).poll());
        notifyAll();
    }

    public synchronized void set() {
        while (storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(LocalDate.now());
        System.out.printf("Set: %d\n",storage.size());
        notifyAll();
    }

    public static void main(String[] args) {
        final EventStorage storage = new EventStorage();
        new Thread(new Consumer(storage)).start();
        new Thread(new Producer(storage)).start();
    }
}

class Producer implements Runnable {
    private EventStorage storage;

    public Producer(final EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.set();
        }
    }
}

class Consumer implements Runnable {
    private EventStorage storage;

    public Consumer(final EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.get();
        }
    }
}
