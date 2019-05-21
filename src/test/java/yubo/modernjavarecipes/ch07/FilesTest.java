package yubo.modernjavarecipes.ch07;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@Slf4j
public class FilesTest {

    @Test
    public void longest5Word() {
        final String path = getClass().getClassLoader().getResource("xanadu.txt").getPath();
        log.info("Get stream from path: {}", path);

        try (final Stream<String> lines = Files.lines(Paths.get(path))) {
            final List<String> results = lines.filter(line -> line.length() > 5)
                    .sorted(Comparator.comparingInt(String::length).reversed())
                    .limit(5)
                    .peek(word -> log.info("line: {}", word))
                    .collect(Collectors.toList());

            Assert.assertThat(results.size(), is(greaterThan(1)));
        } catch (IOException e) {
            log.error("Error encountered!!!", e);
            Assert.fail();
        }
    }
}
