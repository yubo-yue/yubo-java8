package yubo.v1.java7threadcookbook.ch07;

import java.util.concurrent.TimeUnit;

class MyTask implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
