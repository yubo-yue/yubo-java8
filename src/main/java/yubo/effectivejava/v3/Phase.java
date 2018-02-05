package yubo.effectivejava.v3;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public enum Phase {
    SOLID, LIQUID, GAS, PLASMA;

    public enum Transition {
        MELT(SOLID, LIQUID),
        FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS),
        CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS),
        DEPOSIT(GAS, SOLID),
        IONIZATION(GAS, PLASMA),
        DEIONIZATION(PLASMA, GAS);


        @Getter
        private final Phase from;
        @Getter
        private final Phase to;

        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }


        private static final Map<Phase, Map<Phase, Transition>> TRANSITIONS =
                Stream.of(values())
                        .collect(
                                groupingBy(t -> t.from
                                        , () -> new EnumMap<>(Phase.class)
                                        , toMap(t -> t.to
                                                , t -> t
                                                , (x, y) -> y
                                                , () -> new EnumMap<>(Phase.class))
                                )
                        );

        public static Transition from(Phase from, Phase to) {
            return TRANSITIONS.get(from).get(to);
        }

    }

    public static void main(String[] args) {
        System.out.printf("%s -> %s : %s%n", GAS, PLASMA, Transition.from(GAS, PLASMA));
        System.out.printf("%s -> %s : %s%n", GAS, SOLID, Transition.from(GAS, SOLID));
    }
}
