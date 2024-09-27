package site.ithinkso.file_sharing_system.service.thumbnail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.repository.FileRepository;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class ThumbnailService {

    private final FileRepository fileRepository;
    private final List<ThumbnailHandler> thumbnailHandlers;

    public ThumbnailService(FileRepository fileRepository, List<ThumbnailHandler> thumbnailHandlers) {
        this.fileRepository = fileRepository;
        this.thumbnailHandlers = thumbnailHandlers;
    }

    @Async("thumbnailExecutor")
    public void createThumbnail(FileEntity fileEntity) {
        String fileName = fileEntity.getName();
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        if (fileExtension == null) {
            log.info("Failed to extract extension for file {}", fileEntity.getStoreFullPath());
            return;
        }

        File file = null; // cache
        for (ThumbnailHandler thumbnailHandler : thumbnailHandlers) {
            if (thumbnailHandler.supported(fileExtension)) {
                if (file == null) {
                    file = fileEntity.getFile();
                }

                try {
                    String thumbnailFullPath = thumbnailHandler.generateThumbnail(file);
                    fileEntity.setThumbnailPath(thumbnailFullPath);
                    fileRepository.updateEntity(fileEntity);
                    log.info("Thumbnail generated for file: {}", fileEntity.getStoreFullPath());

                    return;
                } catch (Exception e) {
                    log.error("Exception occurred while generating thumbnail. File path={}, handler={}",
                            fileEntity.getStoreFullPath(), thumbnailHandler.getClass().getName(), e);
                }
            }
        }
        log.info("Failed to generate thumbnail for file: {}", fileEntity.getStoreFullPath());
    }
}
