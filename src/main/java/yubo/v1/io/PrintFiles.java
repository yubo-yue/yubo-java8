package yubo.v1.io;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.FileVisitResult.CONTINUE;

public class PrintFiles extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (attrs.isSymbolicLink()) {
            System.out.format("Symbolic link: %s", file);
        } else if (attrs.isRegularFile()) {
            System.out.format("Regular file: %s", file);
        } else {
            System.out.format("Other: %s", file);
        }
        System.out.println("(" + attrs.size() + " bytes");
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc);
        return CONTINUE;
    }

    public static void main(String[] args) {
        final Path startingPath = Paths.get("/Users/yubo/git/yubo-java8/src");
        PrintFiles pf = new PrintFiles();
        EnumSet<FileVisitOption> opts = EnumSet.of(FOLLOW_LINKS);

        try {
            Files.walkFileTree(startingPath, opts, Integer.MAX_VALUE, pf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
