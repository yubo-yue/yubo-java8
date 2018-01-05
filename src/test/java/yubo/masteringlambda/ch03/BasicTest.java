package yubo.masteringlambda.ch03;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalDouble;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BasicTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicTest.class);

    @BeforeClass
    public static void setUp() {
        BasicConfigurator.configure();
    }

    @Test
    public void test1() {
        Stream<Integer> si = IntStream.iterate(1, i -> i * 2).limit(10).boxed();
        assertThat(si, notNullValue());
    }

    @Test
    public void test2() {
        OptionalDouble result = IntStream.rangeClosed(1, 5).map(i -> i + 1).asLongStream().asDoubleStream().max();
        LOGGER.info("max is {}", result.getAsDouble());
    }
}
