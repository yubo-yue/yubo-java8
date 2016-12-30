package yubo.v1.io.path;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

public class DiskUsage {

    private static final long K = 1024;

    private static void printFileStore(final FileStore fileStore) throws IOException {
        long total = fileStore.getTotalSpace() / K;
        long used = (fileStore.getTotalSpace() - fileStore.getUnallocatedSpace()) / K;
        long avail = fileStore.getUsableSpace() / K;

        System.out.format("%-20s %12d %12d %12d\n", fileStore.toString(), total, used, avail);
    }

    public static void main(String[] args) throws IOException {
        System.out.format("%-20s %12s %12s %12s\n", "Filesystem", "kbytes", "used", "avail");
        if (args.length == 0) {
            FileSystem fileSystem = FileSystems.getDefault();
            for (FileStore store : fileSystem.getFileStores()) {
                printFileStore(store);
            }
        } else {
            Arrays.asList(args).stream().forEach(f -> {
                try {
                    FileStore curStore = Files.getFileStore(Paths.get(f));
                    printFileStore(curStore);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
