package yubo.v1.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WatchDir {
    private final WatchService watchService;
    private final Map<WatchKey, Path> keys;
    private final boolean recursive;
    private boolean trace = false;

    public WatchDir(final Path dir, boolean recursive) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.recursive = recursive;

        if (recursive) {
            System.out.printf("Scaning %s ...%n", dir);
            registerAll(dir);
            System.out.println("Done");
        } else {
            register(dir);
        }
        this.trace = true;
    }

    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void register(final Path dir) throws IOException {
        final WatchKey key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            final Path prev = keys.get(key);
            if (null == prev) {
                System.out.printf("register: %s%n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.printf("update: %s -> %s %n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    public void processEvents() {
        for (; ; ) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                return;
            }

            final Path dir = keys.get(key);
            if (null == dir) {
                System.err.println("EventKey is not regonized");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == OVERFLOW)
                    continue;

                WatchEvent<Path> ev = cast(event);
                final Path name = ev.context();
                final Path child = dir.resolve(name);
                System.out.format("%s: %s\n", event.kind().name(), child);

                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        }
    }

    private static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0 || args.length > 2) {
            usage();
        }

        boolean recursive = false;
        int dirArg = 0;

        if (args[0].equals("-r")) {
            if (args.length < 2) {
                usage();
            }
            recursive = true;
            dirArg = 1;
        }

        Path dir = Paths.get(args[dirArg]);
        new WatchDir(dir, recursive).processEvents();
    }
}
