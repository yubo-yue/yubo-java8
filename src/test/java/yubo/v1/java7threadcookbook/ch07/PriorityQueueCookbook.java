package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueueCookbook {
    private static class MyPriorityTransferQueue<E> extends PriorityBlockingQueue<E>
            implements TransferQueue<E> {

        private AtomicInteger counter;
        private LinkedBlockingDeque<E> transferred;
        private ReentrantLock lock;

        public MyPriorityTransferQueue() {
            counter = new AtomicInteger(0);
            lock = new ReentrantLock();
            transferred = new LinkedBlockingDeque<>();
        }

        @Override
        public boolean tryTransfer(E e) {
            lock.lock();
            boolean value;
            if (counter.get() == 0) {
                value = false;
            } else {
                put(e);
                value = true;
            }
            lock.unlock();
            return value;
        }

        @Override
        public void transfer(E e) throws InterruptedException {
            lock.lock();
            if (counter.get() != 0) {
                put(e);
                lock.unlock();
            } else {
                transferred.add(e);
                lock.unlock();
                synchronized (e) {
                    e.wait();
                }
            }
        }

        @Override
        public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
            lock.lock();

            if (counter.get() != 0) {
                put(e);
                lock.unlock();
                return true;
            } else {
                transferred.add(e);
                long newTimeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
                lock.unlock();
                e.wait(newTimeout);
                lock.lock();
                if (transferred.contains(e)) {
                    transferred.remove(e);
                    lock.unlock();
                    return false;
                } else {
                    lock.unlock();
                    return true;
                }
            }
        }

        @Override
        public boolean hasWaitingConsumer() {
            return counter.get() != 0;
        }

        @Override
        public int getWaitingConsumerCount() {
            return counter.get();
        }

        @Override
        public E take() throws InterruptedException {
            lock.lock();
            counter.incrementAndGet();
            E value = transferred.poll();
            if (null == value) {
                lock.unlock();
                value = super.take();
                lock.lock();
            } else {
                synchronized (value) {
                    value.notify();
                }
            }

            counter.decrementAndGet();
            lock.unlock();
            return value;
        }
    }

    @AllArgsConstructor
    private static class Event implements Comparable<Event> {
        @Getter
        private String thread;
        @Getter
        private int priority;

        @Override
        public int compareTo(Event o) {
            if (this.priority > o.getPriority()) {
                return -1;
            } else if (this.priority < o.getPriority()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @AllArgsConstructor
    private static class Producer implements Runnable {
        private MyPriorityTransferQueue<Event> buffer;

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                Event event = new Event(Thread.currentThread().getName(), i);
                buffer.put(event);
            }
        }
    }

    @AllArgsConstructor
    private static class Consumer implements Runnable {
        private MyPriorityTransferQueue<Event> buffer;

        @Override
        public void run() {
            for (int i = 0; i < 1002; i++) {
                try {
                    Event event = buffer.take();
                    System.out.printf("Consumer: %s: %d\n", event.getThread(), event.getPriority());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final MyPriorityTransferQueue<Event> buffer = new MyPriorityTransferQueue<>();
        final Producer producer = new Producer(buffer);

        Thread[] producers = new Thread[10];
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Thread(producer);
            producers[i].start();
        }

        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.
                getWaitingConsumerCount());

        Event myEvent = new Event("Core Event", 0);
        buffer.transfer(myEvent);
        System.out.printf("Main: My Event has ben transfered.\n");

        for (int i = 0; i < producers.length; i++) {
            producers[i].join();
        }

        System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.
                getWaitingConsumerCount());

        TimeUnit.SECONDS.sleep(5);
        myEvent = new Event("Core Event 2", 0);
        buffer.transfer(myEvent);
        consumerThread.join();
        System.out.printf("Main: End of the program\n");
    }

}
