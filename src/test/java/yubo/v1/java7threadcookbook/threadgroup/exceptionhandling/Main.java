package yubo.v1.java7threadcookbook.threadgroup.exceptionhandling;

public class Main {
    public static void main(String[] args) {
        final MyThreadGroup tg = new MyThreadGroup("MyThreadGroup");
        final Task task = new Task();

        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(tg, task);
            t.start();
        }
    }
}
