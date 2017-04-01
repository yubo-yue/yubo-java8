package yubo.fpij.ch03;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class ComparatorTest {
    private List<Person> people;
    private Comparator<Person> ascendingComparatorByAge;
    private Comparator<Person> descendingComparatorByAge;
    private Comparator<Person> ascendingComparatorByName;

    @Before
    public void setUp() {
        people = Arrays.asList(
                new Person("Jone", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 25)
        );
        ascendingComparatorByAge = (p1, p2) -> p1.ageDifference(p2);
        descendingComparatorByAge = ascendingComparatorByAge.reversed();
        ascendingComparatorByName = (p1, p2) -> p1.getName().compareTo(p2.getName());
    }

    @Test
    public void testSortByAge() {
        List<Person> ascendingByAge = people.stream()
                .sorted(ascendingComparatorByAge)
                .collect(toList());

        assertThat(ascendingByAge.get(0).getAge(), is(lessThan(ascendingByAge.get(1).getAge())));
    }

    @Test
    public void testSortByAgeDescending() {
        final List<Person> ascendingByAge = people.stream()
                .sorted(descendingComparatorByAge)
                .collect(toList());

        assertThat(ascendingByAge.get(0).getAge(), is(greaterThan(ascendingByAge.get(1).getAge())));
    }

    @Test
    public void testMin() {
        final Optional<Person> youngest = people.stream().min(Person::ageDifference);
        assertThat(youngest.isPresent(), is(true));
        assertThat("Jone", is(equalTo(youngest.get().getName())));
        assertThat(20, is(equalTo(youngest.get().getAge())));
    }

    @Test
    public void testGroupBy() {
        Map<Integer, List<Person>> peopleByAge = people.stream().collect(groupingBy(Person::getAge));
        assertThat(peopleByAge.get(21), hasSize(2));
    }

    @Test
    public void testGroupByAndMap() {
        Map<Integer, List<String>> peopleByAge =
                people.stream().collect(groupingBy(Person::getAge, mapping(Person::getName, toList())));
        assertThat(peopleByAge.get(21), hasSize(2));

    }

    @Test
    public void testFiles() throws IOException {
        Files.list(Paths.get(".")).filter(Files::isDirectory).forEach(System.out::println);
        Files.newDirectoryStream(Paths.get("src"), path -> path.toString().endsWith(".java")).forEach(System.out::println);
    }

    @Test
    public void testCompareByNameAndAge() {
        final Function<Person, Integer> byAge = p -> p.getAge();
        final Function<Person, String> byName = p -> p.getName();

        final Optional<Person> result = people.stream().sorted(comparing(byAge).thenComparing(byName)).findFirst();
        assertThat(result.isPresent(), is(true));
        final Map<Character, Optional<Person>> oldestPersonEachLetter =
                people.stream().collect(groupingBy(p -> p.getName().charAt(0), reducing(maxBy(comparing(byAge)))));
        Map<Integer, List<Person>> rrr = people.stream().collect(groupingBy(p -> p.getAge(), toList()));
        assertThat(rrr.get(Integer.valueOf(21)), is(hasSize(2)));
        assertThat(oldestPersonEachLetter.get('J').isPresent(), is(true));
        assertThat(oldestPersonEachLetter.get('J').get().getAge(), is(equalTo(21)));
    }

    @Test
    public void testMap() {
        final Map<String, String> maps = new TreeMap<>();
        maps.put("alpha", "X");
        maps.put("beta", "Y");
        maps.put("gamma", "Z");
        final String str = "alpha-beta-gamma";
        maps.replaceAll(str::replace);
        maps.values().stream().forEach(System.out::println);
        maps.replaceAll(String::concat);
        maps.values().stream().forEach(System.out::println);
    }

    @AllArgsConstructor
    private static final class Person {
        @Getter
        @NonNull
        private final String name;

        @Getter
        @NonNull
        private final int age;

        public int ageDifference(final Person other) {
            return age - other.age;
        }

        @Override
        public String toString() {
            return String.format("%s - %d", name, age);
        }
    }
}
