package yubo.java8_impatient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CollectorsTest {

    @Test
    public void test() {
        Map<String, String> resultMap = Arrays.asList(new Pair("key1", "str"), new Pair("key2", null)).stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> a));

        assertThat(resultMap.size(), is(Matchers.greaterThanOrEqualTo(1)));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Pair {
        private String key;
        private String value;

    }
}
