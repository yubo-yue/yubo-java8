package yubo.v1.generic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GenericMethodTest {
    @Test
    public void testGenericCopy() {
        Integer[] ints = new Integer[]{1, 2, 3, 4, 5};
        List<Integer> copy = new ArrayList<>();
        GenericMethodHelper.genericFromArrayToCollection(ints, copy);

        assertThat(copy, is(hasSize(5)));
        assertThat(copy, contains(equalTo(ints[0]), equalTo(ints[1]), equalTo(ints[2]), equalTo(ints[3]), equalTo(ints[4])));
    }

    public void testInference() {
        Integer[] ia = new Integer[100];
        Float[] fa = new Float[100];
        Number[] na = new Number[100];
        Collection<Number> cn = new ArrayList<>();

        GenericMethodHelper.genericFromArrayToCollection(ia, cn);

    }
}


class GenericMethodHelper {

    public static void fromArrayToCollection(Object[] a, Collection<?> c) {
        for (Object o : a) {
//            c.add(o); //compilation error
        }
    }

    public static <T> void genericFromArrayToCollection(T[] a, Collection<T> c) {
        for (T o : a) {
            c.add(o);
        }
    }
}
