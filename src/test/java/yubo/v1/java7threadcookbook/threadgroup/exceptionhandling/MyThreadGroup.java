package yubo.v1.java7threadcookbook.threadgroup.exceptionhandling;

public class MyThreadGroup extends ThreadGroup {

    public MyThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf("the thread %s has thrown exception\n", t.getId());
        e.printStackTrace(System.out);
        System.out.printf("Terminating rest of threads in group \n");
        interrupt();
    }
}
