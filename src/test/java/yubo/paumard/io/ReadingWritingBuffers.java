package yubo.paumard.io;

import com.sun.tools.doclets.standard.Standard;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class ReadingWritingBuffers {

    public static void main(String[] args) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

        buffer.putInt(10);
        buffer.putInt(20);
        buffer.putInt(30);

        log.info("Position = {}, Limit = {}", buffer.position(), buffer.limit());
        buffer.flip();
        log.info("Position = {}, Limit = {}", buffer.position(), buffer.limit());


        log.info("Position = {}, Limit = {}", buffer.position(), buffer.limit());


        final Path path = Paths.get("ints.bin");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            buffer.flip();
            fileChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("File = {}", Files.size(path));

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            buffer.clear();
            fileChannel.read(buffer);
        } catch (IOException e) {
        }

        log.info("Position = {}, Limit = {}", buffer.position(), buffer.limit());

        IntBuffer intBuffer = buffer.asIntBuffer();

        int i = intBuffer.get();

        log.info("Position = {}, Limit = {}", buffer.position(), buffer.limit());
    }
}
