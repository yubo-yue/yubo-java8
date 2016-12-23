package yubo.v1.stream;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StreamTester {
    @Test
    public void createStreamInDifferentWay() {
        Stream unlimitedInteger = Stream.iterate(1, i -> i * 2);
        Optional<Integer> result =
                unlimitedInteger.findFirst();
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is (1));
    }
}
