package yubo.v1.io.path;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class FileTest {

    @Test
    public void checkFileOrDirectoryExist() {
        final Path path = Paths.get("src/test/resources/usnumber.txt");

        assertThat(Files.exists(path), is(true));
        assertThat(Files.notExists(path), is(false));

        assertThat(Files.isDirectory(path), is(false));
        assertThat(Files.isReadable(path), is(true));
        assertThat(Files.isRegularFile(path), is(true));
        assertThat(Files.isExecutable(path), is(false));
    }

    @Test
    public void isSameFile() throws IOException {
        final Path path = Paths.get("src/test/resources/usnumber.txt");
        final Path path1 = Paths.get("src/../src/../src/test/resources/usnumber.txt");

        assertThat(Files.isSameFile(path, path1), is(true));
    }

    @Test
    public void copyFile() {
        final Path source = Paths.get("src/test/resources/usnumber.txt");
        final Path target = Paths.get("src/test/resources/target.txt");

        try {
            Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            fail("copy failed from " + source + " to " + target);
        }

        assertThat(Files.exists(target), is(true));
        try {
            Files.delete(target);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void metadata() throws IOException {
        final Path path = Paths.get("src/test/resources/usnumber.txt");

        assertThat(Files.size(path), is(Matchers.greaterThan(0L)));
        assertThat(Files.isSymbolicLink(path), is(false));
        assertThat(Files.isHidden(path), is(false));
        assertThat(Files.isWritable(path), is(true));
        FileTime lastModifiedTime = Files.getLastModifiedTime(path);
        final UserPrincipal owner = Files.getOwner(path);
        assertThat(owner.getName(), is(equalTo("yubo")));
        System.out.println(lastModifiedTime);
    }

    @Test
    public void getPosixFilePermission() {
        final Path path = Paths.get("src/test/resources/usnumber.txt");

        try {
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path);

            assertThat(permissions, is(notNullValue()));
            assertThat(permissions, is(not(empty())));

            assertThat(permissions.stream().filter(p -> p.equals(PosixFilePermission.OWNER_WRITE)).count(),
                    is(equalTo(1L)));
        } catch (IOException e) {
            fail("get posix file permission failed.");
        }
    }

    @Test
    public void readBasicFileAttributes() {
        final Path path = Paths.get("src/test/resources/usnumber.txt");

        try {
            final BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

            assertThat(basicFileAttributes.isDirectory(), is(false));
            assertThat(basicFileAttributes.isRegularFile(), is(true));
            assertThat(basicFileAttributes.isOther(), is(false));
            assertThat(basicFileAttributes.isSymbolicLink(), is(false));
            assertThat(basicFileAttributes.size(), is(greaterThan(0L)));
            assertThat(basicFileAttributes.creationTime(), is(notNullValue()));
            assertThat(basicFileAttributes.lastModifiedTime(), is(notNullValue()));
            assertThat(basicFileAttributes.lastAccessTime(), is(notNullValue()));


            assertThat(basicFileAttributes, is(notNullValue()));
        } catch (IOException e) {
            fail("read file attributes error.");
        }
    }

    @Test
    public void setLastModifiedTime() {
        final Path path = Paths.get("src/test/resources/usnumber.txt");

        long currentTs = System.currentTimeMillis();
        final FileTime updatedModifiedTime = FileTime.fromMillis(currentTs);

        try {
            Files.setLastModifiedTime(path, updatedModifiedTime);

            final FileTime lastModifiedTime = Files.getLastModifiedTime(path);

            assertThat(lastModifiedTime.to(TimeUnit.DAYS), is(equalTo(updatedModifiedTime.to(TimeUnit.DAYS))));
        } catch (IOException e) {
            fail("set last modified time failed");
        }
    }
}
