package yubo.paumard.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class UsingCharsets {

    public static void main(String[] args) throws IOException {
        Charset latin1 = StandardCharsets.ISO_8859_1;
        Charset utf8 = StandardCharsets.UTF_8;

        String hello = "Hello world from Jose";

        CharBuffer charBuffer = CharBuffer.allocate(1024 * 1024);

        charBuffer.put(hello);
        charBuffer.flip();

        log.info("Position = {}, Limit = {}", charBuffer.position(), charBuffer.limit());

        ByteBuffer buffer = utf8.encode(charBuffer);
        Path path = Paths.get("files/hello-utf8.txt");

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            channel.write(buffer);

        } catch (IOException e) {
        }

        log.info("File size = {}", Files.size(path));

        buffer.clear();
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            channel.read(buffer);

        } catch (IOException e) {
        }
        buffer.flip();
        charBuffer = utf8.decode(buffer);
        final String result = new String(charBuffer.array());

        log.info("The result is : {}", result);
    }
}
