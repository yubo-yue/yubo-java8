package yubo.v1.java7threadcookbook.uncontrolledexceptioninthread;

public class Main {

    public static void main(String[] args) {
        final Task task = new Task();
        Thread t = new Thread(task);
        t.setUncaughtExceptionHandler(new ExceptionHandler());
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        t.start();
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            Integer.parseInt("TTT");
        }
    }
}


