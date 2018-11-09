package yubo.paumard.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class IOUtils {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final String resourceName = "xanadu.txt";


        URL url = IOUtils.getResource(resourceName, IOUtils.class.getClass());

        log.info("Url : {}, to URI: {}", url, url.toURI());

        final Path path = Paths.get(url.toURI());
        log.info("Path: {}", path);

        final BufferedReader reader = Files.newBufferedReader(path);

        reader.lines().count();
    }


    public static URL getResource(final String resourceName, final Class callingClass) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (Objects.isNull(url)) {
            url = IOUtils.class.getClassLoader().getResource(resourceName);
        }

        if (Objects.isNull(url)) {
            ClassLoader cl = callingClass.getClassLoader();

            if (Objects.nonNull(cl)) {
                url = cl.getResource(resourceName);
            }
        }


        if ((Objects.isNull(url))
                && (Objects.nonNull(resourceName))
                && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
            return getResource('/' + resourceName, callingClass);
        }
        return url;
    }
}
