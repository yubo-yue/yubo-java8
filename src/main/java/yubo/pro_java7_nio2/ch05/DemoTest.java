package yubo.pro_java7_nio2.ch05;

import com.sun.javafx.binding.StringFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DemoTest {

    public static void main(String[] args) {
        final Path home = Paths.get(System.getProperty("user.home"));
        final ListVistor walker = new ListVistor();

        try {
            Files.walkFileTree(home, walker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ListVistor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            System.out.println(String.format("visted file %s", file.toString()));
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
            System.out.println(String.format("going to visit directory %s", dir.toString()));
            return super.preVisitDirectory(dir, attrs);
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            System.out.println(String.format("visted directory %s", dir.toString()));
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
            System.out.println(String.format("visit file %s failed", file.toString()));
            return FileVisitResult.TERMINATE;
        }
    }
}
