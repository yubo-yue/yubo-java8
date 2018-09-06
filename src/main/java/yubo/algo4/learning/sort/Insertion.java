package yubo.algo4.learning.sort;

public class Insertion {

    public static void sort(final Comparable[] a) {
        int n = a.length;

        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && SortCommons.less(a[j], a[j - 1]); j--) {
                SortCommons.exch(a, j, j - 1);
            }
        }
    }
}
