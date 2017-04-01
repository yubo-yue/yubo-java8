package yubo.masteringlambda.ch03;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;
import java.math.BigInteger;
import java.time.Year;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toConcurrentMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DemoTest {

    private List<Book> library;

    @Before
    public void setUp() {
        library = new ArrayList<>();
        final Book nails = new Book(
                "Fundamentals of Chinese Fingernail Image",
                Arrays.asList("Li", "Fu", "Li"),
                new int[]{256},
                Topic.MEDICINE,
                Year.of(2014),
                25.2);
        final Book dragon = new Book(
                "Compilers: Principles, Techniques and Tools",
                Arrays.asList("Aho", "Lam", "Sethi", "Ullman"),
                new int[]{2006},
                Topic.COMPUTING,
                Year.of(2014),
                23.6);
        final Book voss = new Book(
                "Voss",
                Arrays.asList("Patric White"),
                new int[]{478},
                Topic.FICTION,
                Year.of(1957),
                19.8);
        final Book lotr = new Book(
                "Lord of the Rings",
                Arrays.asList("Tolkien"),
                new int[]{531, 416, 124},
                Topic.FICTION,
                Year.of(1955),
                23.0);

        library.add(nails);
        library.add(dragon);
        library.add(voss);
        library.add(lotr);
    }

    @Test
    public void test1() {
        final Stream<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).parallelStream();

        OptionalDouble retOptional = ints.map(i -> new Point(i % 3, i / 3)).mapToDouble(p -> p.distance(0, 0)).max();
        assertThat(retOptional.isPresent(), is(true));

        final List<Integer> integers = IntStream.range(1, 11).boxed().collect(toList());
    }

    @Test
    public void test2() {
        Stream<Book> computingBooks = library.stream().filter(b -> b.getTopic() == Topic.COMPUTING);
        Stream<String> bookTitles = library.stream().map(b -> b.getTitle());
        Stream<Book> booksSortedByTitle = library.stream().sorted(comparing(b -> b.getTitle()));
        Stream<String> authorsInBookTitleOrder = library.stream().
                sorted(comparing(b -> b.getTitle())).flatMap(b -> b.getAuthors().stream()).distinct();
        Optional<Book> oldest = library.stream().min(comparing(b -> b.getPubDate()));
        Set<String> titles = library.stream().map(b -> b.getTitle()).collect(Collectors.toSet());
        assertThat(oldest.isPresent(), is(true));
        assertThat(oldest.get().getTopic(), is(Topic.FICTION));
        assertThat(titles, is(hasSize(4)));

        boolean withinShelfHeight = library.stream().filter(b -> b.getTopic() == Topic.HISTORY).anyMatch(b -> b.getHeight() < 19);
        assertThat(withinShelfHeight, is(false));
    }

    @Test
    public void test3() {
        final Map<String, Year> titleToPubDate = library.stream()
                .collect(
                        toMap(
                                Book::getTitle,
                                Book::getPubDate,
                                maxBy(naturalOrder()),
                                TreeMap::new
                        ));
        assertThat(titleToPubDate.get("Voss"), is(notNullValue()));
        final List<String> authorsForBooks = library.stream()
                .map(b -> b.getAuthors().stream().collect(joining(",", b.getTitle() + ":", "\n"))).collect(toList());
        System.out.println(authorsForBooks);
    }

    @Test
    public void test4() {
        Optional<Set<Topic>> mostPopularTopics = library.stream()
                .collect(groupingBy(Book::getTopic, counting())).entrySet()
                .stream().collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toSet())))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey()).map(Map.Entry::getValue);
        assertThat(mostPopularTopics.isPresent(), is(true));
    }

    @Test
    public void test5() {
        final Supplier<Deque<DispRecord>> supplier = ArrayDeque::new;
        final BiConsumer<Deque<DispRecord>, Book> accumulator = (dqLeft, b) -> {
            int disp = dqLeft.isEmpty() ? 0 : dqLeft.getLast().totalDisp();

            dqLeft.add(new DispRecord(
                    b.getTitle(),
                    disp,
                    Arrays.stream(b.getPageCounts()).sum()
            ));
        };
        final BinaryOperator<Deque<DispRecord>> combiner = (left, right) -> {
            if (left.isEmpty()) return right;
            int newDisp = left.getLast().totalDisp();
            List<DispRecord> displacedRecords = right.stream()
                    .map(dr -> new DispRecord(dr.title, dr.disp + newDisp, dr.length))
                    .collect(toList());
            left.addAll(displacedRecords);
            return left;
        };
        final Function<Deque<DispRecord>, Map<String, Integer>> finisher =
                ddr -> ddr.parallelStream().collect(toConcurrentMap(dr -> dr.title, dr -> dr.disp));

        final Map<String, Integer> displacementMap = library.stream().collect(Collector.of(supplier, accumulator, combiner, finisher));
        assertThat(displacementMap.get("Voss"), is(equalTo(Integer.valueOf(2262))));
        assertThat(displacementMap.get("Lord of the Rings"), is(equalTo(Integer.valueOf(2262 + 478))));
    }

    @Test
    public void test6() {
        int sum = IntStream.rangeClosed(1, 10).sum();
        int otherSum = IntStream.rangeClosed(1, 10).reduce(0, (i1, i2) -> i1 + i2);
        assertThat(sum, is(equalTo(otherSum)));
        int min = IntStream.rangeClosed(1, 10).min().getAsInt();
        int otherMin = IntStream.rangeClosed(1, 10).reduce(Math::min).getAsInt();
        assertThat(min, is(equalTo(otherMin)));

        Optional<BigInteger> bigIntegerSum =
                LongStream.of(1, 2, 3).mapToObj(BigInteger::valueOf).reduce(BigInteger::add);
        BigInteger otherBigIntegerSum = LongStream.of(1, 2, 3)
                .mapToObj(BigInteger::valueOf).reduce(BigInteger.ZERO, BigInteger::add);
        assertThat(bigIntegerSum.isPresent(), is(true));
        assertThat(bigIntegerSum.get(), is(equalTo(otherBigIntegerSum)));
    }

    @Test
    public void test7() {
        int totalVolue = library.stream().mapToInt(b -> b.getPageCounts().length).sum();
        int totalValueInOtherWay = library.stream()
                .reduce(0, (sum, book) -> sum + book.getPageCounts().length, Integer::sum);

        assertThat(totalVolue, is(equalTo(totalValueInOtherWay)));
    }

    @AllArgsConstructor
    @Getter
    private static class Book {
        private String title;
        private List<String> authors;
        private int[] pageCounts;
        private Topic topic;
        private Year pubDate;
        private double height;
    }

    private enum Topic {
        HISTORY, MEDICINE, COMPUTING, FICTION
    }

    @AllArgsConstructor
    private static class DispRecord {
        private final String title;
        private final int disp, length;

        public int totalDisp() {
            return disp + length;
        }
    }
}
