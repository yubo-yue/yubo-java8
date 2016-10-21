package yubo.v1.jcip.composingobject;

import java.util.concurrent.atomic.AtomicInteger;

public class NumberRange {
    private AtomicInteger lower;
    private AtomicInteger upper;

    public void setLower(int i) {
        if (i > upper.get()) {
            throw new IllegalStateException();
        }

        this.lower.set(i);
    }

    public void setUpper(int i) {
        if (i < lower.get()) {
            throw new IllegalStateException();
        }

        this.upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
