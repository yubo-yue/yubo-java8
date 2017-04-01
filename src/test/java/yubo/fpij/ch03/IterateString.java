package yubo.fpij.ch03;

import org.junit.Test;

public class IterateString {
    @Test
    public void testChars() {
        final String message = "HelloWorld 2017!";
        message.chars().forEach(ch -> System.out.println(ch));
        message.chars().filter(Character::isDigit).forEach(System.out::println);
    }
}
