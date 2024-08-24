package site.ithinkso.file_sharing_system.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.domain.MetaData;
import site.ithinkso.file_sharing_system.domain.Structure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DataListResponse {

    private String id;
    private long fileCount;
    private long directoryCount;
    private long totalByteSize;
    private String directoryFullPath;

    private List<FileData> files = new ArrayList<>();
    private List<DirectoryData> directories = new ArrayList<>();

    public DataListResponse(DirectoryEntity directoryEntity) {
        this.id = directoryEntity.getId();
        this.fileCount = directoryEntity.getFileCount();
        this.directoryCount = directoryEntity.getDirectoryCount();
        this.totalByteSize = directoryEntity.getTotalByteSize();
        this.directoryFullPath = directoryEntity.getDirectoryFullPath();

        List<MetaData> children = directoryEntity.getChildren();
        for (MetaData metaData : children) {
            if (metaData.getStructure() == Structure.FILE) {
                FileEntity file = (FileEntity) metaData;
                FileData fileData = new FileData(file);
                this.files.add(fileData);
            }
            else {
                DirectoryEntity directory = (DirectoryEntity) metaData;
                DirectoryData directoryData = new DirectoryData(directory);
                this.directories.add(directoryData);
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FileData {
        private String id;
        private String name;
        private LocalDateTime createdAt;

        private long byteSize;
        private String thumbnailFullPath;

        public FileData(FileEntity fileEntity) {
            this.id = fileEntity.getId();
            this.name = fileEntity.getName();
            this.createdAt = fileEntity.getCreatedAt();
            this.byteSize = fileEntity.getByteSize();
            this.thumbnailFullPath = fileEntity.getThumbnailFullPath();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DirectoryData {
        private String id;
        private String name;
        private LocalDateTime createdAt;

        private long fileCount;
        private long directoryCount;
        private long totalByteSize;
        private String directoryFullPath;

        public DirectoryData(DirectoryEntity directoryEntity) {
            this.id = directoryEntity.getId();
            this.name = directoryEntity.getName();
            this.createdAt = directoryEntity.getCreatedAt();
            this.fileCount = directoryEntity.getFileCount();
            this.directoryCount = directoryEntity.getDirectoryCount();
            this.totalByteSize = directoryEntity.getTotalByteSize();
            this.directoryFullPath = directoryEntity.getDirectoryFullPath();
        }
    }
}
