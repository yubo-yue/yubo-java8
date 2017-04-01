package yubo.masteringlambda.ch05;

import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DemoTest {

    @Test
    public void test1() {
        IntStream alternatingSign = IntStream.iterate(1, i -> -i);
        int first = alternatingSign.findFirst().getAsInt();

        assertThat(first, is(equalTo(1)));
    }

    @Test
    public void test2() {
        int[] ints = {2, 3, 4, 5, 6};
        assertThat(20, is(equalTo(Arrays.stream(ints).sum())));

        long[] longs = {1, 2, 3, 4, 5, 6};
        assertThat(21L, is(equalTo(Arrays.stream(longs).sum())));

        final IntStream is = IntStream.of(ints);
        assertThat(20, is(equalTo(is.sum())));
    }

    @Test
    public void test3() {
        byte[] bits = {10, 18};

        BitSet bs = BitSet.valueOf(bits);
        List<Integer> integers = bs.stream().boxed().collect(toList());
        assertThat(integers, hasSize(4));
    }

    @Test
    public void test4() throws IOException {
        final String start = "";

        try (Stream<Path> paths = Files.list(Paths.get(start))) {
            paths.flatMap(path -> {
                Stream<String> lines;

                try {
                    lines = Files.lines(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    lines = Stream.of("Unreadable file: " + path);
                }
                return lines;
            }).forEach(line -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }
}
