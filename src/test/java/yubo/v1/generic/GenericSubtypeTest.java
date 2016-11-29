package yubo.v1.generic;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GenericSubtypeTest {

    @Test
    public void testGenericAndSubtyping() {
        List<String> ls = new ArrayList<>();
        //List<Objects> lo = ls; //list of string is not list of object
        List<?> lQ = new ArrayList<Integer>();
        lQ.add(0, null); // null is subtype of any type, including unknow type "?"
        assertThat(lQ.get(0), nullValue());
    }

    @Test
    public void testDraw() {
        List<Shape> ss = Arrays.asList(new Circle(), new Rectangle());
        Helper.draw(ss);

        List<Circle> cs = Arrays.asList(new Circle(), new Circle());
        //Helper.draw(cs);
        Helper.drawAny(cs);

        assertTrue("can be draw", true);
    }

}

class Helper {
    public static void printCollection(final Collection<?> collection) {
        for (Object e : collection) {
            System.out.println(e);
        }
    }

    /**
     * can only accept list of Shape
     * @param ss
     */
    public static void draw(final List<Shape> ss) {
        for (Shape s : ss) {
            s.draw();
        }
    }

    /**
     * can draw on list of any shape
     * @param ss
     */
    public static void drawAny(final List<? extends Shape> ss) {
        for (Shape s : ss) {
            s.draw();
        }
        //ss.add(new Circle());
    }
}

abstract class Shape {
    public abstract void draw() ;
}

class Circle extends Shape {
    @Override
    public void draw() {
        //
    }
}

class Rectangle extends Shape {
    @Override
    public void draw() {

    }
}
