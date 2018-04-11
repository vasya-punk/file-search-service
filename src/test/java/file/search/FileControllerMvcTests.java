package file.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Value("${root}")
    private String rootDestination;

    private Path folder1;
    private Path folder2;
    private Path file1;
    private Path file2;

    @Before
    public void init() throws Exception{
        folder1 = Files.createTempDirectory(Paths.get(rootDestination),"folder1");
        folder2 = Files.createTempDirectory(folder1,"folder2");
        file1 = Files.createTempFile(folder2,"file1","suf");
        file2 = Files.createTempFile(folder2,"file2","suf");
    }

    @Test
    public void getFilesByNameReturnedListSubfoldersWithFiles() throws Exception {
        this.mockMvc.perform(get("/files/getFilesByDirectoryName")
                        .param("name", folder1.toFile().getName())
                        .with(user("user").password("password").roles("USER"))
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.."+folder1+"."+folder2).value(2));
    }

    @Test
    public void getFilesByPathReturnedArrayOfFiles() throws Exception {
        this.mockMvc.perform(get("/files/getFilesByDirectoryPath")
                        .param("path", folder2.toString())
                        .with(user("user").password("password").roles("USER"))
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getFileBasicAttributesReturnedBasicAttributes() throws Exception {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file1, BasicFileAttributes.class);

        this.mockMvc.perform(get("/files/getFileBasicAttributes")
                        .param("path", file1.toString())
                        .with(user("user").password("password").roles("USER"))
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.lastModifiedTime").value(basicFileAttributes.lastModifiedTime().toString()));
    }

    @After
    public void finish() throws Exception{
        Files.delete(file1);
        Files.delete(file2);
        Files.delete(folder2);
        Files.delete(folder1);
    }
}
