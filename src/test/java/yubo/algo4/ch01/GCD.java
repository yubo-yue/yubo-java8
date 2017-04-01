package yubo.algo4.ch01;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GCD {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public static int gcd(int p, int q) {
        if (q < 0 || q < 0) {
            throw new IllegalArgumentException("p and q must be greater than equal to 0");
        }
        if (q == 0) {
            return p;
        }

        int r = p % q;
        return gcd(q, r);
    }

    @Test
    public void test10_0() {
        int r = gcd(10, 0);
        assertThat(r, is(10));
    }

    @Test
    public void test0_10() {
        int r = gcd(0, 10);
        assertThat(r, is(10));
    }

    @Test
    public void test100_10() {
        int r = gcd(100, 10);
        assertThat(r, is(10));
    }

    @Test
    public void test9_6() {
        int r = gcd(9, 6);
        assertThat(r, is(3));
    }

    @Test
    public void throwIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        gcd(-1, 10);
    }
}
