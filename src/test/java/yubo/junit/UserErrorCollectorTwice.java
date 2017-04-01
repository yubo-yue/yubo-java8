package yubo.junit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class UserErrorCollectorTwice {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void testXXX() {
        collector.addError(new Throwable("First thing went to error"));
        collector.addError(new Throwable("Second thing went to error"));
    }
}
