package site.ithinkso.file_sharing_system.domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DirectoryEntity extends MetaData {

    private long fileCount;
    private long directoryCount;
    private long totalByteSize;
    private String directoryFullPath;

    @DBRef(lazy = true)
    private List<MetaData> children;

    public DirectoryEntity(String name, LocalDateTime createdAt, DirectoryEntity parent, String directoryFullPath) {
        super(name, Structure.DIRECTORY, createdAt, parent);
        this.fileCount = 0;
        this.directoryCount = 0;
        this.totalByteSize = 0;
        this.directoryFullPath = directoryFullPath;
        this.children = new ArrayList<>();
    }

    public void addChildren(List<? extends MetaData> metaData) {
        for (MetaData metaDatum : metaData) {
            if (metaDatum.getStructure() == Structure.FILE) {
                this.fileCount++;
                this.totalByteSize += ((FileEntity) metaDatum).getByteSize();
            }
            else {
                this.directoryCount++;
            }
        }
        this.children.addAll(metaData);
    }

    public void increaseFileInformation(long fileCount, long totalByteSize) {
        this.fileCount += fileCount;
        this.totalByteSize += totalByteSize;
    }

    public void increaseDirectoryCount(long directoryCount) {
        this.directoryCount += directoryCount;
    }

    public boolean isRoot() {
        return this.getParent() == null;
    }
}
