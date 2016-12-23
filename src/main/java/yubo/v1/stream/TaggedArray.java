package yubo.v1.stream;

import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public class TaggedArray<T> {
    private final Object[] elements;

    public TaggedArray(final T[] data, final Object[] tags) {
        int size = data.length;

        if (tags.length != size) throw new IllegalArgumentException();
        this.elements = new Object[size * 2];

        for (int i = 0, j = 0; i < size; ++i) {
            elements[j++] = data[i];
            elements[j++] = tags[i];
        }
    }

    public Spliterator<T> spliterator() {
        return new TaggedArraySpliterator<>(elements, 0, elements.length);
    }

    static class TaggedArraySpliterator<T> implements Spliterator<T> {
        private final Object[] array;
        private int index;
        private final int fence;

        TaggedArraySpliterator(final Object[] array, int origin, int fence) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            for (; index < fence; index += 2) {
                action.accept((T) array[index]);
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (index < fence) {
                action.accept((T) array[index]);
                index += 2;
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            int lo = index;
            int mid = ((lo + fence) >>> 1) & ~1; // force midpoint to be even

            if (lo < mid) {
                return new TaggedArraySpliterator<>(array, lo, mid);
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return (long) ((fence - index) / 2);
        }

        @Override
        public int characteristics() {
            return ORDERED | SIZED | IMMUTABLE | SUBSIZED;
        }
    }

    public static <T> void parEach(final TaggedArray<T> a, Consumer<T> action) {
        Spliterator<T> s = a.spliterator();
        long targetBatchSize = s.estimateSize() / (ForkJoinPool.getCommonPoolParallelism() * 8);
        new ParEach<>(null, s, action, targetBatchSize).invoke();
    }

    static class ParEach<T> extends CountedCompleter<Void> {
        final Spliterator<T> spliterator;
        final Consumer<T> consumer;
        final long targetBatchSize;

        ParEach(ParEach<T> parent, Spliterator<T> spliterator, Consumer<T> consumer,
                long targetBatchSize) {
            super(parent);
            this.spliterator = spliterator;
            this.consumer = consumer;
            this.targetBatchSize = targetBatchSize;
        }

        @Override
        public void compute() {
            Spliterator<T> sub;
            while (spliterator.estimateSize() > targetBatchSize && (sub = spliterator.trySplit()) != null) {
                addToPendingCount(1);
                propagateCompletion();
            }
        }
    }
}
