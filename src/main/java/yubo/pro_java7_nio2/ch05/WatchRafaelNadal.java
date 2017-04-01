package yubo.pro_java7_nio2.ch05;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class WatchRafaelNadal {
    public static void main(String[] args) {
        final Path path = Paths.get("/tmp");
        WatchRafaelNadal watch = new WatchRafaelNadal();
        watch.watchRNDir(path);
    }

    public void watchRNDir(final Path path) {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                final WatchKey key = watchService.take();
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    final WatchEvent.Kind<?> kind = watchEvent.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    final Path filename = (Path) watchEvent.context();
                    System.out.println(String.format("%s -> %s", kind, filename));
                }

                boolean valid = key.reset();
                if (!valid)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
