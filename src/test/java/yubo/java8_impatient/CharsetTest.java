package yubo.java8_impatient;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;

public class CharsetTest {
    @Test
    public void test() {
        Charset defaultCharset = Charset.defaultCharset();
        System.out.println(defaultCharset);
    }

    @Test
    public void test1() {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }
    }
}
