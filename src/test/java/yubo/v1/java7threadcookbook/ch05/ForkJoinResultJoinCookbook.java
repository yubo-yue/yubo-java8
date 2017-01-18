package yubo.v1.java7threadcookbook.ch05;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinResultJoinCookbook {
    public static void main(String[] args) {
        final Document document = new Document();
        String[][] doc = document.generateDocument(100, 1000, "the");

        DocumentTask task = new DocumentTask(doc, 0, 100, "the");
        final ForkJoinPool pool = new ForkJoinPool();

        pool.execute(task);

        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", pool.
                    getParallelism());
            System.out.printf("Main: Active Threads: %d\n", pool.
                    getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", pool.
                    getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n", pool.
                    getStealCount());
            System.out.printf("******************************************\n");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        pool.shutdown();

        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.printf("Main: The word appears %d in the document%n", task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class Document {
        private String[] words = {"the", "hello", "goodbye", "packt", "java", "t hread", "pool", "random", "class", "main"};

        public String[][] generateDocument(int numLines, int numWords, final String word) {
            int counter = 0;
            final String[][] document = new String[numLines][numWords];
            final Random random = new Random();

            for (int i = 0; i < numLines; i++) {
                for (int j = 0; j < numWords; j++) {
                    int curIndex = random.nextInt(words.length);
                    document[i][j] = words[curIndex];

                    if (document[i][j].equals(word)) {
                        counter++;
                    }
                }
            }
            System.out.printf("The word %s occurs %d times.%n", word, counter);
            return document;
        }
    }

    @AllArgsConstructor
    private static class DocumentTask extends RecursiveTask<Integer> {
        private final String[][] document;
        private final int start, end;
        private final String word;

        @Override
        protected Integer compute() {
            int result = 0;

            if (end - start < 10) {
                result = processLines(document, start, end, word);
            } else {
                int mid = (start + end) / 2;
                DocumentTask task1 = new DocumentTask(document, start, mid, word);
                DocumentTask task2 = new DocumentTask(document, mid, end, word);
                invokeAll(task1, task2);

                try {
                    result = groupResults(task1.get(), task2.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        private int groupResults(int a, int b) {
            return a + b;
        }

        private Integer processLines(final String[][] document, int start, int end, final String word) {
            final List<LineTask> lineTasks = new ArrayList<>();
            for (int i = start; i < end; i++) {
                final LineTask curTask = new LineTask(document[i], 0, document[i].length, word);
                lineTasks.add(curTask);
            }
            invokeAll(lineTasks);

            int result = 0;
            for (int i = 0; i < lineTasks.size(); i++) {
                final LineTask curTask = lineTasks.get(i);
                try {
                    result = result + curTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return Integer.valueOf(result);
        }
    }

    @AllArgsConstructor
    private static class LineTask extends RecursiveTask<Integer> {
        private final String[] line;
        private final int start, end;
        private final String word;

        @Override
        protected Integer compute() {
            int result = 0;
            if (end - start < 100) {
                result = count(line, start, end, word);
            } else {
                int mid = (start + end) / 2;
                final LineTask task1 = new LineTask(line, start, mid, word);
                final LineTask task2 = new LineTask(line, mid, end, word);

                invokeAll(task1, task2);
                try {
                    result = groupResult(task1.get(), task2.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        private int groupResult(int a, int b) {
            return a + b;
        }

        private int count(final String[] line, final int start, final int end, final String word) {
            int counter = 0;
            for (int i = start; i < end; i++) {
                if (word.equals(line[i])) {
                    counter ++;
                }
            }
            return counter;
        }
    }
}
