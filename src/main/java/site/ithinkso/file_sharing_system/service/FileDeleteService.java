package site.ithinkso.file_sharing_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.repository.DirectoryRepository;
import site.ithinkso.file_sharing_system.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileDeleteService {

    private final FileRepository fileRepository;
    private final DirectoryRepository directoryRepository;

    public boolean deleteById(String id) {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        DirectoryEntity parent = fileEntity.getParent();
        parent.deleteChild(fileEntity);
        directoryRepository.updateEntity(parent);

        boolean fileDeleteResult = fileRepository.deleteById(id);

        if (parent.isRoot()) {
            return fileDeleteResult;
        }

        DirectoryEntity grandparent = parent.getParent();
        directoryRepository.updateDirectoryUptoRoot(grandparent.getId(), -1, 0, -fileEntity.getByteSize());

        return fileDeleteResult;
    }
}
