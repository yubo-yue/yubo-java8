package yubo.effectivejava.v3;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Operation {
    PLUS("+") {
        @Override
        public double apply(final double x, final double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        public double apply(final double x, final double y) {
            return x - y;
        }
    },
    TIMES("*") {
        @Override
        public double apply(final double x, final double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        public double apply(final double x, final double y) {
            return x / y;
        }
    };

    private final String symbol;

    Operation(final String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    private static final Map<String, Operation> STRING_TO_ENUM = Stream.of(Operation.values()).collect(Collectors.toMap(Object::toString, e -> e));

    public static Optional<Operation> fromString(final String symbol) {
        return Optional.ofNullable(STRING_TO_ENUM.get(symbol));
    }

    public abstract double apply(double x, double y);

    public static void main(String[] args) {
        double x = 1.0f;
        double y = 2.0f;

        for (Operation op : Operation.values()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }
}
