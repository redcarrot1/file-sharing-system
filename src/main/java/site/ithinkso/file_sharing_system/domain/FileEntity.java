package site.ithinkso.file_sharing_system.domain;

import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Path;
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

    public Path getPath() {
        Path path = Path.of(storeFullPath);
        if (!Files.exists(path)) {
            throw new IllegalStateException("File is not physically exists");
        }
        return path;
    }

    public void setThumbnailPath(String thumbnailFullPath) {
        this.thumbnailFullPath = thumbnailFullPath;
    }
}
