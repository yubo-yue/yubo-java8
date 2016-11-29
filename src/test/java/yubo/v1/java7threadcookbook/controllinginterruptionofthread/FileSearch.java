package yubo.v1.java7threadcookbook.controllinginterruptionofthread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class FileSearch implements Runnable {
    private String initPath;
    private String fileName;

    public FileSearch(final String initPath, final String fileName) {
        requireNonNull(initPath);
        requireNonNull(fileName);

        this.initPath = initPath;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        Path p = Paths.get(initPath);

        if (Files.isDirectory(p)) {
            try {
                processDirectory(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processDirectory(final Path p) throws InterruptedException {
        try (final Stream<Path> s = Files.list(p)
        ) {
            s.forEach(elem -> {
                if (Files.isDirectory(elem)) {
                    try {
                        processDirectory(elem);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        processFile(elem);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    private void processFile(final Path p) throws InterruptedException {
        if (p.getFileName().endsWith(fileName)) {
            System.out.printf("%s: %s\n", Thread.currentThread().getName(), p.toAbsolutePath());
        }

        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    public static void main(String[] args) {
        final FileSearch searcher = new FileSearch("/tmp", "aaa.txt");

        Thread t = new Thread(searcher);
        t.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }
}
