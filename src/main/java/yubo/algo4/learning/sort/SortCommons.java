package yubo.algo4.learning.sort;

import org.apache.commons.lang3.time.StopWatch;
import yubo.algo4.common.StdOut;

import java.util.Arrays;
import java.util.stream.Stream;

public class SortCommons {

    public static boolean less(final Comparable v, final Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static void exch(final Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void show(final Comparable[] a) {
        Stream.of(a).forEach(
                item -> StdOut.print(item + " ")
        );
        StdOut.println();
    }

    public static long time(final String alg, Comparable[] a) {
        StopWatch timer = new StopWatch();
        timer.start();

        if (alg.equalsIgnoreCase("Insertion")) Insertion.sort(a);
        if (alg.equalsIgnoreCase("Selection")) Selection.sort(a);

        long nonatime = timer.getNanoTime();
        timer.stop();

        return nonatime;
    }

    public static void main(String[] args) {
        StdOut.println("Insertion:" + time("Insertion", new Integer[]{5, 8, 9, 3, 2, 1}));
        StdOut.println("Selection:" + time("Selection", new Integer[]{5, 8, 9, 3, 2, 1}));
    }
}
