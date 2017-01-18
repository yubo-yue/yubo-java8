package yubo.v1.java7threadcookbook.ch07;

public class CustomizedThreadFactoryCookbook {
    public static void main(String[] args) {
        final MyThreadFactory threadFactory = new MyThreadFactory("mythread");
        final MyTask task = new MyTask();

        final Thread t = threadFactory.newThread(task);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: Thread information.\n");
        System.out.printf("%s\n", t);
        System.out.printf("Main: End of the example.\n");

    }

}
