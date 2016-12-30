package yubo.v1.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Find {
    public static class Finder extends SimpleFileVisitor<Path> {
        private final PathMatcher matcher;

        private int numMatches = 0;

        Finder(final String pattern) {
            this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        private void find(final Path file) {
            Path name = file.getFileName();
            if (null != name && matcher.matches(name)) {
                numMatches++;
                System.out.println("file: " + file);
            }
        }

        private void done() {
            System.out.println("Matched: " + numMatches);
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            find(file);
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path file, final IOException e) throws IOException {
            System.err.println(e);
            return CONTINUE;
        }
    }

    private static void usage() {
        System.err.println("java Find <path>" +
                " -name \"<glob_pattern>\"");
        System.exit(-1);
    }

    public static void main(String[] args) {
        if (args.length < 3 || !args[1].equals("-name"))
            usage();

        final Path startDir = Paths.get(args[0]);
        final String pattern = args[2];

        final Finder finder = new Finder(pattern);
        try {
            Files.walkFileTree(startDir, finder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finder.done();
    }
}
