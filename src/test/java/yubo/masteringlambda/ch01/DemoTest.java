package yubo.masteringlambda.ch01;

import org.junit.Test;

import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DemoTest {

    @Test
    public void testComposition() {
        final List<Integer> ints = Arrays.asList(1, 2, 3);
        final Comparator<Point> compareByX = DemoTest.comparing(p -> p.getX());
        Optional<Point> optional = ints.stream().map(i -> new Point(i % 3, i / 3)).max(compareByX);

        assertThat("input has maximum point", optional.isPresent(), is(true));
        assertThat(optional.get(), is(equalTo(new Point(2, 0))));

        final Comparator<Point> compareByDistance = DemoTest.comparing(p -> p.distance(0f, 0f));
        Optional<Point> maxPointByDistance = ints.stream().map(i -> new Point(i % 3, i / 3)).max(compareByDistance);
        assertThat("input has maximum point", maxPointByDistance.isPresent(), is(true));
        assertThat(maxPointByDistance.get(), is(equalTo(new Point(2, 0))));

    }

    public static <T, U extends Comparable<U>> Comparator<T> comparing(final Function<T, U> keyExtractor) {
        return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }
}
