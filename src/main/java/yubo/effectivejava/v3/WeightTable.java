package yubo.effectivejava.v3;

public class WeightTable {
    public static void main(String[] args) {
        double earthWeight = 2.399f;

        double mass = earthWeight / Planet.EARTH.surfaceGravity();

        for (Planet p : Planet.values()) {
            System.out.printf("Weight on %s is %f%n", p, p.surfaceWeight(mass));
        }
    }
}
