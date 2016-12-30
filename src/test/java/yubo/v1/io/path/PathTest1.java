package yubo.v1.io.path;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PathTest1 {

    @Test
    public void createPathObject() {
        final Path p1 = Paths.get("/tmp/foo");
        final Path p2 = Paths.get(URI.create("file:///Users/yubo/test.txt"));
        final Path p3 = FileSystems.getDefault().getPath("/tmp/foo");
        final Path p4 = Paths.get(System.getProperty("user.home"), "test.txt");

        assertThat(p1, is(equalTo(p3)));
        assertThat(p2, is(equalTo(p4)));
    }

    @Test
    public void retrievePathParts() {
        final Path p1 = Paths.get("/home/joe/foo");
        assertThat(p1.toString(), is(equalTo("/home/joe/foo")));
        assertThat(p1.getFileName().toString(), is(equalTo("foo")));
        assertThat("getName(0):", p1.getName(0).toString(), is(equalTo("home")));
        assertThat("getNameCount()", p1.getNameCount(), is(3));
        assertThat("subpath(0, 2)", p1.subpath(0, 2).toString(), is(equalTo("home/joe")));
        assertThat("getParent:", p1.getParent().toString(), is(equalTo("/home/joe")));
        assertThat("getRoot:", p1.getRoot().toString(), is(equalTo("/")));
    }

    @Test
    public void convertPath() {
        final Path p1 = Paths.get("/tmp/../tmp/../tmp");
        try {
            Path realPath = p1.toRealPath(LinkOption.NOFOLLOW_LINKS);
            assertThat(realPath.toString(), is(equalTo("/tmp/../tmp")));
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
    }

    @Test
    public void joinPath() {
        final Path p1 = Paths.get("/home/joe/foo");
        final Path p2 = p1.resolve("bar");

        assertThat(p2.toString(), is(equalTo("/home/joe/foo/bar")));
    }

    @Test
    public void createPathBetween2Paths() {
        final Path p1 = Paths.get("joe");
        final Path p2 = Paths.get("sally");

        Path p1ToP2 = p1.relativize(p2);
        Path p2ToP1 = p2.relativize(p1);

        assertThat(p1ToP2.toString(), is(equalTo("../sally")));
        assertThat(p2ToP1.toString(), is(equalTo("../joe")));

        final Path p3 = Paths.get("home");
        final Path p4 = Paths.get("home/sally/bar");

        Path p3ToP4 = p3.relativize(p4);
        Path p4ToP3 = p4.relativize(p3);

        assertThat(p3ToP4.toString(), is(equalTo("sally/bar")));
        assertThat(p4ToP3.toString(), is(equalTo("../..")));
    }
}
