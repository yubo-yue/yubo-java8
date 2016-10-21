package yubo.v1.lambda;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OverrideTest {

    @Test
    public void parentDefaultUsed() {
        final Parent p = new ParentImpl();
        p.welcome();
        assertThat("Parent: Hi!", is(equalTo(p.getLastMessage())));
    }

    @Test
    public void childOverrideDefault() {
        final Child c = new ChildImpl();
        c.welcome();
        assertThat("Child: Hi!", is(equalTo(c.getLastMessage())));
    }

    @Test
    public void concreteBeatsDefault() {
        final Parent p = new OverrideParent();
        p.welcome();
        assertThat("Class Parent: Hi!", is(equalTo(p.getLastMessage())));
    }

    @Test
    public void concreteBeatsCloserDefault() {
        final Child c = new OverridingChild();
        c.welcome();
        assertThat("Class Parent: Hi!", is(equalTo(c.getLastMessage())));
    }
}

interface Parent {
    void message(final String message);

    default void welcome() {
        message("Parent: Hi!");
    }

    String getLastMessage();
}

class ParentImpl implements Parent {
    private String body;

    @Override
    public void message(String message) {
        this.body = message;
    }

    @Override
    public String getLastMessage() {
        return body;
    }
}

interface Child extends Parent {
    default void welcome() {
        message("Child: Hi!");
    }
}

class ChildImpl extends ParentImpl implements Child {

}

class OverrideParent extends ParentImpl {
    @Override
    public void welcome() {
        message("Class Parent: Hi!");
    }
}

class OverridingChild extends OverrideParent implements Child {

}
