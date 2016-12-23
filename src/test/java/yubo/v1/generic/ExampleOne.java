package yubo.v1.generic;

import java.util.ArrayList;
import java.util.List;

public class ExampleOne<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static <Z> Z noop(Z z) {
        return z;
    }

    public static void main(String[] args) {
        //List<?> numbers = new ArrayList<Number>();
        //List<? extends Number> numbers = new ArrayList<>();
        List<? super Number> numbers = new ArrayList<>();
//        List<Number> numbers = new ArrayList<>();

        numbers.add(new Integer(4));
       // Number numericValue = numbers.get(0);

    }
}
