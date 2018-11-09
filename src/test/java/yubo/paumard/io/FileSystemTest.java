package yubo.paumard.io;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FileSystemTest {

    public static void main(String[] args) {
        FileSystem defaultFs = FileSystems.getDefault();

        Iterable<Path> roots = defaultFs.getRootDirectories();

        roots.forEach(p -> System.out.println(p));

        Iterable<FileStore> store = defaultFs.getFileStores();

        System.out.println("");
        store.forEach(s -> System.out.println(s));

    }
}
