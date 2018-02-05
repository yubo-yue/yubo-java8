package yubo.effectivejava.v3;

public class Plant {
    enum Lifecycle {ANNUAL, PERENNIAL, BIENNIAL};

    private final String name;
    private final Lifecycle lifecycle;

    public Plant(final String name, final Lifecycle lifecycle) {
        this.name = name;
        this.lifecycle = lifecycle;
    }

    @Override
    public String toString() {
        return name;
    }
}
