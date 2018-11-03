package yubo.algo4.common;

import java.util.Random;

/**
 * Class for generating random numbers from various discrete and continuous distributions,
 * include uniform, Bernouli, geometric, Gaussian, exponential, Pareto, Poisson, and Cauchy.
 */
public final class StdRandom {
    private static Random random;

    private static long seed;


    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    public static Double uniform(double v, double v1) {
        return random.nextDouble();
    }

    public static void setSeed(long s) {
        seed = s;
        random = new Random(seed);
    }

    public static long getSeed() {
        return seed;
    }

}
