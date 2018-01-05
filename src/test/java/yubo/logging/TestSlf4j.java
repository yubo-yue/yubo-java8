package yubo.logging;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class TestSlf4j {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestSlf4j.class);

    @Before
    public void setUp() {
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%r [%t] %p %c - %m%n")));
    }

    @Test
    public void test() {
        LOGGER.info("start to test.");
        assertTrue(true);
        LOGGER.info("end test.");
    }
}
