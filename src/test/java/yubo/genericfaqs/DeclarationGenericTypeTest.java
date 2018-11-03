package yubo.genericfaqs;

import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DeclarationGenericTypeTest {

    private static class Pair<X, Y> {
        private X first;
        private Y second;

        public X getFirst() {
            return first;
        }

        public void setFirst(X first) {
            this.first = first;
        }

        public Y getSecond() {
            return second;
        }

        public void setSecond(Y second) {
            this.second = second;
        }
    }

    @Test
    public void test() {
        Pair<String, Date> firstPair = new Pair<>();

        firstPair.setFirst("Birthday");
        firstPair.setSecond(Date.from(Instant.now()));

        assertThat(firstPair, is(notNullValue()));

        printPair(firstPair);
    }

    @Test
    public void testRuntimeTypeOfGenericType() {
        Class<?> clazzA = new ArrayList<String>().getClass();
        Class<?> clazzB = new ArrayList<Date>().getClass();
        assertThat(clazzA, is(equalTo(clazzB)));
    }

    private void printPair(Pair<?, ?> pair) {
        System.out.println(pair.getFirst() + "," + pair.getSecond());
    }
}
