package site.ithinkso.file_sharing_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileExplorerService {

    private final FileRepository fileRepository;

    public DirectoryEntity findDirectory(String directoryPath) {
        // TODO Lazy loading이 언제 발생되는가?
        return fileRepository.findDirectoryByPath(directoryPath);
    }
}
