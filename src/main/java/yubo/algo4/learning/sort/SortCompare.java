package yubo.algo4.learning.sort;

import org.apache.commons.lang3.time.StopWatch;
import yubo.algo4.common.StdOut;
import yubo.algo4.common.StdRandom;

public class SortCompare {

    public static double time(final String alg, final Double[] a) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        if (alg.equals("Insertion")) Insertion.sort(a);
        else if (alg.equals("Selection")) Selection.sort(a);
        else
            throw new IllegalArgumentException("Invalid algorithm: " + alg);

        stopWatch.stop();
        return stopWatch.getTime();
    }

    public static double timeRandomInput(final String alg, int n, int trials) {
        double total = 0.0;

        Double[] a = new Double[n];

        for (int t = 0; t < trials; t++) {
            for (int i = 0; i < n; i++) {
                a[i] = StdRandom.uniform(0.0, 1.0);
            }
            total += time(alg, a);
        }
        return total;
    }

    public static double timeSortedInput(final String alg, int n, int trials) {
        double total = 0.0;
        Double[] a = new Double[n];

        for (int t = 0; t < trials; t++) {
            for (int i = 0; i < n; i++) {
                a[i] = 1.0 * i;
            }
            total += time(alg, a);
        }

        return total;
    }

    /**
     * program algo1 algo2 n trials [optional sorted]
     *
     * @param args
     */
    public static void main(String[] args) {
        String alg1 = args[0];
        String alg2 = args[1];

        int n = Integer.parseInt(args[2]);
        int trials = Integer.parseInt(args[3]);


        double time1, time2;
        if (args.length == 5 && args[4].equals("sorted")) {
            time1 = timeSortedInput(alg1, n, trials);
            time2 = timeSortedInput(alg2, n, trials);
        } else {
            time1 = timeRandomInput(alg1, n, trials);
            time2 = timeRandomInput(alg2, n, trials);
        }

        StdOut.printf("For %d random Doubles\n %s is", n, alg1);
        StdOut.printf(" %.1f times faster than %s\n", time2/time1, alg2);
    }
}
