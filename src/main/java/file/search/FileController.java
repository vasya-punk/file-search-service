package file.search;

import file.search.dto.BasicFileAttributesJson;
import file.search.exceptions.CustomRestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "files", description = "Get files and folders with path on your server")
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${root}")
    private String rootDestination;

    @ApiOperation(value = "Returns a list of all of the subdirectories with paths and files counts")
    @ApiResponse(code = 200, message = "Successfully get files by directory name")
    @GetMapping(value = "/getFilesByDirectoryName")
    public Map<String, Map<String, Long>> getFilesByName(@RequestParam("name") String name) {
        final Path root = Paths.get(rootDestination);

        Map<String, Map<String, Long>> result = null;
        try {
            result = Files.walk(root)
                    .filter(path -> Files.isDirectory(path) && path.toFile().getName().equals(name)) // filter out only needed folders with given name
                    .collect(Collectors.toMap(Path::toString, p-> { // collect map of our folders with values of maps with subfolders and files inside
                        Map<String, Long> res = null;
                        try {
                            // map subfolders to files inside
                            res = Files.walk(p).filter(pp-> !pp.equals(p) && Files.isDirectory(pp)).collect(Collectors.toMap(Path::toString, pp-> {
                                Long cnt = 0L;
                                try {
                                    cnt = Files.list(pp).filter(Files::isRegularFile).count();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return cnt;
                            }));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return res;
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @ApiOperation(value = "Given a path to a particular directory the service returns a list of files in that directory")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get files by directory path"),
            @ApiResponse(code = 500, message = "if some server error or given path not a directory")
    })
    @GetMapping("/getFilesByDirectoryPath")
    public List<String> getFilesByPath(@RequestParam("path") String path) {
        final Path root = Paths.get(path);
        if(!Files.isDirectory(root.resolve(path))) {
            throw new CustomRestException(path+" - is not a directory!");
        }

        List<String> result = null;
        try {
            result = Files.list(root).filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            result.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @ApiOperation(value = "Given a path to a particular file name the service returns all of the standard file attributes for that file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get files by directory path"),
            @ApiResponse(code = 500, message = "if some server error or given path not a file")
    })
    @GetMapping("/getFileBasicAttributes")
    public BasicFileAttributesJson getFileBasicAttributes(@RequestParam("path") String path) {
        final Path root = Paths.get(rootDestination);
        if(!Files.isRegularFile(root.resolve(path))) {
            throw new CustomRestException(path+" - is not a file!");
        }

        BasicFileAttributes result = null;
        try {
            result = Files.readAttributes(root.resolve(path), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BasicFileAttributesJson(result);
    }
}
