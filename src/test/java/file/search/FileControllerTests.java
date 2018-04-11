package file.search;

import file.search.exceptions.CustomRestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileControllerTests {
    @Value("${root}")
    private String rootDestination;

    @Autowired
    private FileController fileController;

    @Rule
    public ExpectedException notFolder = ExpectedException.none();

    private Path folder1;
    private Path file1;

    @Before
    public void init() throws Exception{
        folder1 = Files.createTempDirectory(Paths.get(rootDestination),"folder1");
        file1 = Files.createTempFile(folder1,"file1","suf");
    }

    @Test
    public void getFilesByPathThrowException() {
        notFolder.expect(CustomRestException.class);
        notFolder.expectMessage(file1+" - is not a directory!");

        fileController.getFilesByPath(file1.toString());
    }

    @Test
    public void getFileBasicAttributesThrowException() {
        notFolder.expect(CustomRestException.class);
        notFolder.expectMessage(folder1+" - is not a file!");

        fileController.getFileBasicAttributes(folder1.toString());
    }

    @After
    public void finish() throws Exception{
        Files.delete(file1);
        Files.delete(folder1);
    }
}
