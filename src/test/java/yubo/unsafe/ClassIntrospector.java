package yubo.unsafe;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Map;


public class ClassIntrospector {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClassIntrospector.class);

    private static final Unsafe THE_UNSAFE;
    private static final int OBJECT_REF_SIZE;

    static {
        try {
            final PrivilegedExceptionAction<Unsafe> action = () -> {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return (Unsafe) theUnsafe.get(null);
            };
            THE_UNSAFE = AccessController.doPrivileged(action);

            OBJECT_REF_SIZE = THE_UNSAFE.arrayIndexScale(Object[].class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    public static Unsafe getUnsafe() {
        return THE_UNSAFE;
    }

    public static int getObjectRefSize() {
        return OBJECT_REF_SIZE;
    }

    private static final Map<Class, Integer> primitiveSizes;

    static {
        primitiveSizes = new HashMap<>(10);
        primitiveSizes.put(byte.class, 1);
        primitiveSizes.put(char.class, 2);
        primitiveSizes.put(int.class, 4);
        primitiveSizes.put(long.class, 8);
        primitiveSizes.put(float.class, 4);
        primitiveSizes.put(double.class, 8);
        primitiveSizes.put(boolean.class, 1);
    }

    public static void main(String[] args) {
        /**
         * Initialized logger system.
         */
        BasicConfigurator.configure();

        final int scale = THE_UNSAFE.arrayIndexScale(Object[].class);

        LOGGER.info("Array index scale of Object[] class is {}", scale);

        final int addressSize = THE_UNSAFE.addressSize();
        LOGGER.info("Address size is {}", addressSize);

        LOGGER.info("Object Ref size is {}", getObjectRefSize());

    }
}
