package yubo.junit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HasTempFolder {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void usingTempFoler() throws IOException {
        File createdFile = temporaryFolder.newFile("Myfile.txt");
        File createdFolder = temporaryFolder.newFolder("subfolder");
        assertThat(createdFile, is(notNullValue()));
    }
}
