package yubo.pro_java7_nio2.ch02;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DemoTest {
    public static void main(String[] args) {
//        testDefaultFSView();
//        testViewOnSpecificFileStore();
//        testCreateDirectories();
//        testListDirectory();
//        testUserDefinedFilter();
//        testTimeFilter();
        testCreateReadWrite();
    }

    public static void testDefaultFSView() {
        FileSystem fs = FileSystems.getDefault();
        Set<String> views = fs.supportedFileAttributeViews();
        views.stream().forEach(System.out::println);
    }

    public static void testViewOnSpecificFileStore() {
        FileSystem fs = FileSystems.getDefault();
        for (FileStore fileStore : fs.getFileStores()) {
            boolean supported = fileStore.supportsFileAttributeView(DosFileAttributeView.class);
            System.out.println(fileStore.name() + " ---- " + supported);
        }
    }

    public static void testCreateDirectories() {
        Path newpath = Paths.get("/tmp/", "nio2/test1/");
        try {
            Files.createDirectories(newpath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.delete(newpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testListDirectory() {
        Path path = Paths.get("/tmp");
        System.out.println("No filter applied");

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testUserDefinedFilter() {
        final Path userHome = Paths.get(System.getProperty("user.home"));

        DirectoryStream.Filter<Path> dirFilter = path -> Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);

        System.out.println("User defined filter applied");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(userHome, dirFilter)) {
            ds.forEach(p -> System.out.println(p.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testTimeFilter() {
        final Path userHome = Paths.get(System.getProperty("user.home"));
        DirectoryStream.Filter<Path> timeFilter = p -> {
            long currentTime = FileTime.fromMillis(System.currentTimeMillis()).to(TimeUnit.DAYS);
            long modifiedTime = ((FileTime) Files.getAttribute(p, "basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS)).to(TimeUnit.DAYS);

            if (currentTime == modifiedTime)
                return true;
            return false;
        };

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(userHome, timeFilter)) {
            ds.forEach(p -> System.out.println(p.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testCreateReadWrite() {
        final Path newFile = Paths.get(System.getProperty("java.io.tmpdir") + "/test.txt");
        final Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        final FileAttribute<Set<PosixFilePermission>> attrs = PosixFilePermissions.asFileAttribute(perms);

        System.out.println(System.getProperty("java.io.tmpdir"));
        try {
            Files.createFile(newFile, attrs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
