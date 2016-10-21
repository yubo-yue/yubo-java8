package yubo.v1.jcip.immutability;

import java.util.HashSet;
import java.util.Set;

/**
 * Immutable class built out of mutable underlying objects.
 */
public class ThreeStooges {
    private final Set<String> stooges = new HashSet<>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooges(final String name) {
        return stooges.contains(name);
    }
}
