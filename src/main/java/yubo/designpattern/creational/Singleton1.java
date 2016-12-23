package yubo.designpattern.creational;

public class Singleton1 {
    private volatile static Singleton1 INSTANCE;

    public static Singleton1 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton1.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton1();
                }
            }
        }
        return INSTANCE;
    }
}
