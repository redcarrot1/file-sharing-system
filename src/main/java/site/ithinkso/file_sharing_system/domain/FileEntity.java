package site.ithinkso.file_sharing_system.domain;

import lombok.Getter;

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
}
