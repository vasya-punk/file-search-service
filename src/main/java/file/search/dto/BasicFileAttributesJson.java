package file.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class BasicFileAttributesJson implements BasicFileAttributes {

    private BasicFileAttributes basicFileAttributes;

    public BasicFileAttributesJson(BasicFileAttributes basicFileAttributes){
        this.basicFileAttributes = basicFileAttributes;
    }

    @JsonRawValue
    public FileTime lastModifiedTime(){
        return basicFileAttributes.lastModifiedTime();
    }

    @JsonRawValue
    public FileTime lastAccessTime(){
        return basicFileAttributes.lastAccessTime();
    }

    @JsonRawValue
    public FileTime creationTime(){
        return basicFileAttributes.creationTime();
    }

    @Override
    public boolean isRegularFile() {
        return basicFileAttributes.isRegularFile();
    }

    @Override
    public boolean isDirectory() {
        return basicFileAttributes.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return basicFileAttributes.isSymbolicLink();
    }

    @Override
    public boolean isOther() {
        return basicFileAttributes.isOther();
    }

    @JsonRawValue
    public long size(){
        return basicFileAttributes.size();
    }

    @Override
    @JsonIgnore
    public Object fileKey() {
        return basicFileAttributes.fileKey();
    }
}
