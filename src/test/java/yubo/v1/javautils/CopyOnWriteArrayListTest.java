package yubo.v1.javautils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class CopyOnWriteArrayListTest {

    @Test
    public void testBasicUsage() {
        final List<Integer> original = Arrays.asList(1, 2, 3, 4, 5);
        final CopyOnWriteArrayList<Integer> underTest = new CopyOnWriteArrayList<>(original);

        assertThat(underTest.size(), is(equalTo(5)));
        assertThat(underTest.get(0), is(equalTo(1)));

        try {
            underTest.get(5);
        } catch (Exception e) {
            assertThat(e, instanceOf(ArrayIndexOutOfBoundsException.class));
        }
        final ListIterator<Integer> oldIterator = underTest.listIterator();

        underTest.set(0, 7);

        while (oldIterator.hasNext()) {
            assertThat(oldIterator.next(), allOf(lessThan(7), greaterThan(0)));
        }

        assertThat(underTest, contains(equalTo(7), lessThan(7), lessThan(7), lessThan(7), lessThan(7)));

        assertThat(original, everyItem(lessThan(Integer.valueOf(7))));
    }
}
