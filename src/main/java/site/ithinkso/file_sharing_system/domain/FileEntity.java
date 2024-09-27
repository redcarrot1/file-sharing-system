package site.ithinkso.file_sharing_system.domain;

import lombok.Getter;

import java.io.File;
import java.time.LocalDateTime;

@Getter
public class FileEntity extends MetaData {

    private long byteSize;
    private String storeFullPath;
    private String thumbnailFullPath;

    public FileEntity(String name, LocalDateTime createdAt, DirectoryEntity parent, long byteSize, String storeFullPath) {
        super(name, Structure.FILE, createdAt, parent);
        this.byteSize = byteSize;
        this.storeFullPath = storeFullPath;
    }

    public File getFile() {
        File file = new File(storeFullPath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not physically exists");
        }
        return file;
    }

    public void setThumbnailPath(String thumbnailFullPath) {
        this.thumbnailFullPath = thumbnailFullPath;
    }
}
