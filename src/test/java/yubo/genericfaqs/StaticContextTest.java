package yubo.genericfaqs;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class StaticContextTest {

    /**
     * Is there one instances of a static field
     * per instantiation of a generic type.
     */
    @Test
    public void test1() {
        SomeClazz<String> someString = new SomeClazz<>();
        SomeClazz<Long> someInt = new SomeClazz<>();

        someString.value++;
        someString.value++;

        Assert.assertThat(someInt.value, Matchers.is(2));
    }
}

class SomeClazz<T> {
    public static int value = 0;
}

