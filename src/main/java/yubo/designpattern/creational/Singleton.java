package yubo.designpattern.creational;

/**
 * http://www.javaworld.com/article/2074979/java-concurrency/double-checked-locking--clever--but-broken.html
 * by Brian Goetz
 */
public class Singleton {

    private Singleton() {}

    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
