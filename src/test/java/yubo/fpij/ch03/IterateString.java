package yubo.fpij.ch03;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

public class IterateString {
    @Test
    public void testChars() {
        final String message = "HelloWorld 2017!";
        message.chars().forEach(ch -> System.out.println(ch));
        message.chars().filter(Character::isDigit).forEach(System.out::println);
    }

    @Test
    public void test() {
        final Integer a = 1;
        final List<String> list = Lists.newArrayList();
        list.add("a");

        final Integer b = 1;

        if (a != b) {
            System.out.println("not equal");
        } else if (a == b) {
            System.out.println("equal");
        }

    }
}
