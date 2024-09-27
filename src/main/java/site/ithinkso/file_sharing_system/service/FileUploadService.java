package site.ithinkso.file_sharing_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.repository.DirectoryRepository;
import site.ithinkso.file_sharing_system.repository.FileRepository;
import site.ithinkso.file_sharing_system.service.thumbnail.ThumbnailService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;
    private final DirectoryRepository directoryRepository;
    private final ThumbnailService thumbnailService;

    public List<FileEntity> saveFiles(String directoryPath, List<MultipartFile> multipartFiles) {
        DirectoryEntity parent = directoryRepository.findByPath(directoryPath)
                .orElseThrow(() -> new IllegalArgumentException("Directory not found"));

        validateDuplicatedName(parent, multipartFiles);

        List<FileEntity> storedFiles = storeFiles(parent, multipartFiles);
        fileRepository.saveAll(storedFiles);
        for (FileEntity fileEntity : storedFiles) {
            thumbnailService.createThumbnail(fileEntity);
        }

        parent.addChildren(storedFiles);
        directoryRepository.updateEntity(parent);

        updateDirectoryUptoRoot(parent, storedFiles);

        return storedFiles;
    }

    private void validateDuplicatedName(DirectoryEntity parent, List<MultipartFile> multipartFiles) {
        boolean isDuplicatedName = multipartFiles.stream()
                .map(MultipartFile::getOriginalFilename)
                .anyMatch(parent::haveChildrenName);
        if (isDuplicatedName) {
            throw new IllegalArgumentException("Duplicated file name");
        }
    }

    private List<FileEntity> storeFiles(DirectoryEntity parent, List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(multipartFile -> {
                    try {
                        return storeFile(parent, multipartFile);
                    } catch (IOException e) {
                        log.error("Error occurred while storing file", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private FileEntity storeFile(DirectoryEntity parent, MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String storeFullPath = getFullPath(storeFileName);

        File file = new File(storeFullPath);
        multipartFile.transferTo(file);

        LocalDateTime createdAt = LocalDateTime.now();
        return new FileEntity(originalFilename, createdAt, parent, multipartFile.getSize(), storeFullPath);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private void updateDirectoryUptoRoot(DirectoryEntity parent, List<FileEntity> storedFiles) {
        if (parent.isRoot()) {
            return;
        }

        long totalByteSize = storedFiles.stream()
                .mapToLong(FileEntity::getByteSize)
                .sum();
        DirectoryEntity grandparent = parent.getParent();
        directoryRepository.updateDirectoryUptoRoot(grandparent.getId(), storedFiles.size(), 0, totalByteSize);
    }


    public String getFullPath(String filename) {
        return fileDir + filename;
    }
}
