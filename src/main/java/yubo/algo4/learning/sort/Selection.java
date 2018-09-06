package yubo.algo4.learning.sort;

public class Selection {
    public static void sort(final Comparable[] a) {
        int n = a.length;

        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (SortCommons.less(a[j], a[min])) min = j;
            }
            SortCommons.exch(a, i, min);
        }
    }

    public static void main(String[] args) {
        final Character[] input = {'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E'};

        sort(input);

        SortCommons.show(input);
    }
}
