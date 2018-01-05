package yubo.masteringlambda.ch02;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MethodReferenceTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MethodReferenceTest.class);

    @BeforeClass
    public static void setUp() {
        BasicConfigurator.configure();
    }

    @Test
    public void testStaticMethodRef() {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1);
        ints.stream().sorted().forEach(e -> LOGGER.info("{}", e));
    }

    @Test
    public void testUnboundInstanceMethodRef() {
        Map<String, String> map = new TreeMap<>();
        map.put("alpha", "X");
        map.put("bravo", "Y");
        map.put("charlie", "Z");

        map.replaceAll(String::concat);
        map.forEach((k, v) -> {
            LOGGER.info("Key {}, Value {}", k, v);
        });
    }
}
