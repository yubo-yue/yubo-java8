package yubo.v1.lambda.optional;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OptionalTest {

    /**
     * Type
     * int a;
     * a -> a + a FunctionalInterface
     * f: a -> a
     * g: a -> a
     *
     *  h(fg(a)) hf(g(a))
     * gf = h: a -> a
     *
     * h(a) = g(fa)
     *
     */
    public static int f(int a) {
        return a + 2;
    }

    public static int g(int a) {
        return a * 2;
    }

    public static int h(int a) {
        return g(f(a));
    }

    @Test
    public void testHFunction() {
        int gfResult = g(f(1));
        int hResult = h(1);

        assertThat(gfResult, is(equalTo(hResult)));
    }

    /**
     *
     * f: a -> Ma
     * g: a -> Ma
     * h: a -> Ma
     *
     * Monad
     * return: a -> Ma
     * Integer -> Optional<Integer>
     */
    @Test
    public void testConstructor() {
        final Optional<Integer> integer = Optional.of(1);

        assertThat(integer.get(), is(1));
    }


    /**
     * f: a -> Ma
     * g: a -> Ma
     * h: a -> Ma
     *
     * \a -> (fa) >>= \a -> (ga)
     *        Ma       a -> Ma    Ma
     *
     * Manod monad Integer integer
     * Optional<T>
     *
     *     Optional<Integer>
     * Monoid
     * a set of things
     * rule
     * meta rule
     *
     * (x + y) % 12
     * ^
     * x ^ y ^ z = x ^ ( y ^ z) associative
     * x ^ 12 = x id
     * 12 ^ x = x
     *
     * Monad
     * bind: Ma -> (a -> Ma) -> Ma
     * Optional<Integer>
     */
    @Test
    public void testMap() {
        final Optional<Integer> result = Optional.of(20)
                .filter(a -> a % 2 == 0).map(a -> a / 2)
                .filter(a -> a % 2 == 0).map(a -> a / 2)
                .filter(a -> a % 2 == 0).map(a -> a / 2);

        assertThat(result.isPresent(), is(false));
    }

    /**
     * a -> Ma
     * >>= bind
     * Ma -> (a -> Mb) -> Mb
     * Mb -> (b -> Mc) -> Mc
     */
    @Test
    public void testMap1() {
        final Optional<Double> doubleValue = Optional.of(1).map(a -> a.doubleValue());

        assertThat(doubleValue.get(), is(equalTo(1.0)));
    }
}
