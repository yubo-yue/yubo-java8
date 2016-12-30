package yubo.v1.java7threadcookbook.ch02.conditionlock;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FileMock {
    private String[] contents;
    private int index;

    public FileMock(int size, int length) {
        contents = new String[size];

        for (int i = 0; i < size; i++) {
            StringBuilder builder = new StringBuilder(length);
            for (int j = 0; j < length; j++) {
                int indice = (int) Math.random() * 255;
                builder.append((char) indice);
            }
            contents[i] = builder.toString();
        }
        index = 0;
    }

    public boolean hasMoreLines() {
        return index < contents.length;
    }

    public String getLine() {
        if (this.hasMoreLines()) {
            System.out.println("Mock: " + (contents.length - index));
            return contents[index++];
        }
        return null;
    }
}

class Buffer {
    private LinkedList<String> buffer;
    private int maxSize;
    private ReentrantLock lock;
    private Condition lines;
    private Condition spaces;

    private boolean pendingLines;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new LinkedList<>();
        lock = new ReentrantLock();
        lines = lock.newCondition();
        spaces = lock.newCondition();
        pendingLines = true;
    }

    public void insert(final String line) {
        lock.lock();

        try {
            while (buffer.size() == maxSize) {
                spaces.await();
            }

            buffer.offer(line);
            System.out.printf("%s Inserted line: %d\n", Thread.currentThread().getName(), buffer.size());
            lines.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String get() {
        String line = null;
        lock.lock();
        try {
            while (buffer.size() == 0 && hasPendingLines()) {
                lines.await();
            }

            if (hasPendingLines()) {
                line = buffer.poll();
                System.out.printf("%s: Line Readed: %d\n", Thread.
                        currentThread().getName(), buffer.size());
                spaces.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return line;
    }

    public void setPendingLines(boolean pendingLines) {
        this.pendingLines = pendingLines;
    }

    public boolean hasPendingLines() {
        return pendingLines || buffer.size() > 0;
    }
}

class Producer implements Runnable {
    private final FileMock fileMock;
    private final Buffer buffer;

    public Producer(final FileMock fileMock, final Buffer buffer) {
        this.fileMock = fileMock;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        buffer.setPendingLines(true);
        while (fileMock.hasMoreLines()) {
            buffer.insert(fileMock.getLine());
        }
        buffer.setPendingLines(false);
    }
}

class Consumer implements Runnable {
    private final Buffer buffer;

    public Consumer(final Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.hasPendingLines()) {
            String line = buffer.get();
            processLine(line);
        }
    }

    private void processLine(String line) {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
