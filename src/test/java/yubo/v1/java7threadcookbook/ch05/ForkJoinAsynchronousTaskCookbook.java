package yubo.v1.java7threadcookbook.ch05;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ForkJoinAsynchronousTaskCookbook {
    public static void main(String[] args) {
        final ForkJoinPool pool = new ForkJoinPool();
        FolderProcessorTask task = new FolderProcessorTask(Paths.get("src/test/java"), "2.java");
        pool.execute(task);

        do {
            System.out.printf("***************************************** *\n");
            System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
            System.out.printf("***************************************** *\n");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());
        pool.shutdown();

        final List<String> result = task.join();
        result.stream().forEach(s -> System.out.println(s));

    }

    @AllArgsConstructor
    private static class FolderProcessorTask extends RecursiveTask<List<String>> {
        private final Path path;
        private final String extension;


        @Override
        protected List<String> compute() {
            final PathMatcher pathMatcher =
                    FileSystems.getDefault().getPathMatcher(String.format("glob:*%s", extension));
            final List<String> files = new ArrayList<>();
            final List<FolderProcessorTask> tasks = new ArrayList<>();
            try {
                final List<Path> paths = Files.list(path).collect(Collectors.toList());
                for (final Path p : paths) {
                    if (Files.isDirectory(p)) {
                        FolderProcessorTask task = new FolderProcessorTask(p.toAbsolutePath(), extension);
                        task.fork();
                        tasks.add(task);
                    } else {
                        if (pathMatcher.matches(p.getFileName())) {
                            files.add(p.toString());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (tasks.size() > 50) {
                System.out.printf("%s: %d tasks ran.\n", path.toAbsolutePath(), tasks.size());
            }

            for (FolderProcessorTask task : tasks) {
                files.addAll(task.join());
            }
            return files;
        }
    }
}
