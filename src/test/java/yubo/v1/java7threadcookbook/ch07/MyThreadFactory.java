package yubo.v1.java7threadcookbook.ch07;

import java.util.concurrent.ThreadFactory;

class MyThreadFactory implements ThreadFactory {
    private int counter;
    private final String prefix;

    public MyThreadFactory(final String prefix) {
        this.prefix = prefix;
        this.counter = 1;
    }

    @Override
    public Thread newThread(final Runnable r) {
        final MyThread thread = new MyThread(r, String.format("%s-%d", prefix, counter));
        return thread;
    }
}
