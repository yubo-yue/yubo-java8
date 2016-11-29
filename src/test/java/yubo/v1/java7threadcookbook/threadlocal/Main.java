package yubo.v1.java7threadcookbook.threadlocal;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
//        UnsafeTask task = new UnsafeTask();
//        for (int i = 0; i < 10; i++) {
//            Thread thread = new Thread(task);
//            thread.start();
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        SafeTask safeTask = new SafeTask();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(safeTask);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
