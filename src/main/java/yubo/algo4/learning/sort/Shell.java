package yubo.algo4.learning.sort;

public class Shell {
    private Shell() {

    }

    public static void sort(final Comparable[] a) {
        int n = a.length;

        int h = 1;
        while (h < n / 3) h = h * 3 + 1;

        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && SortCommons.less(a[j], a[j - h]); j -= h) {
                    SortCommons.exch(a, j, j - h);
                }
            }
        }
    }
}
