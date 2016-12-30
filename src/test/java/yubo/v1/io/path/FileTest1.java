package yubo.v1.io.path;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class FileTest1 {

    @Test
    public void smallFileReadAndWrite() {
        final Path file = Paths.get("src/test/resources/usnumber.txt");
        try {
            byte[] bytes = Files.readAllBytes(file);
            assertThat(bytes.length, is(greaterThan(0)));

            Path out = Paths.get("/tmp/small.txt");
            Files.write(out, bytes);

            assertThat(Files.exists(out), is(true));
        } catch (IOException e) {
            fail("read file error.");
        }
    }

    @Test
    public void testBufferedFileReadAndWrite() {
        final Path file = Paths.get("src/test/resources/usnumber.txt");
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.lines().forEach(System.out::println);

            assertThat(1, is(1));
        } catch (IOException e) {
            fail();
        }

        final Path out = Paths.get("/tmp/buffer.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(out, Charset.forName("US-ASCII"))) {
            writer.write("test");

            assertThat(1, is(1));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void logFilePermissionTest() throws IOException {
        final Set<OpenOption> options = new HashSet<>();

        options.add(StandardOpenOption.APPEND);
        options.add(StandardOpenOption.CREATE);

        final Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r-----");
        final FileAttribute<Set<PosixFilePermission>> attr =
                PosixFilePermissions.asFileAttribute(perms);

        final String s = "Hello World!";
        byte[] data = s.getBytes();
        ByteBuffer bb = ByteBuffer.wrap(data);

        Path file = Paths.get("/tmp/perms.log");
        try (SeekableByteChannel sbc = Files.newByteChannel(file, options, attr)) {
            sbc.write(bb);
        } catch (IOException e) {
            fail();
        } finally {
            Files.delete(file);
        }
    }


    @Test
    public void testTempfile() {
        try {
            final Path tmp = Files.createTempFile(null, ".yubo");

            System.out.printf("temporary file created: " + tmp);
            assertThat(1, is(1));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void probeFileType() {
        final Path file = Paths.get("src/test/resources/usnumber.txt");
        try {
            final String fileType = Files.probeContentType(file);
            assertThat(fileType, is(nullValue()));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void pathSeparator() {
        final String separator = File.separator;
        final String systemSeparator = FileSystems.getDefault().getSeparator();

        assertThat(separator, is(notNullValue()));
        assertThat(systemSeparator, is(notNullValue()));
    }
}
