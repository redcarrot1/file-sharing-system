package site.ithinkso.file_sharing_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.repository.DirectoryRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;

    public DirectoryEntity findDirectory(String path) {
        // TODO Lazy loading이 언제 발생되는가?
        return directoryRepository.findByPath(path)
                .orElseThrow(() -> new IllegalArgumentException("Directory not found"));
    }

    public DirectoryEntity createDirectory(String path, String name) {
        DirectoryEntity parent = directoryRepository.findByPath(path)
                .orElseThrow(() -> new IllegalArgumentException("Directory not found"));

        validateAlreadyExistsName(parent, name);

        String directoryFullPath = createFullPath(path, name);
        LocalDateTime createAt = LocalDateTime.now();
        DirectoryEntity directory = new DirectoryEntity(name, createAt, parent, directoryFullPath);
        directoryRepository.save(directory);

        parent.addChild(directory);
        directoryRepository.updateEntity(parent);

        return directory;
    }

    private void validateAlreadyExistsName(DirectoryEntity parent, String name) {
        parent.getChildren().stream()
                .filter(directory -> directory.getName().equals(name))
                .findAny()
                .ifPresent(directory -> {
                    throw new IllegalArgumentException("Directory already exists");
                });
    }

    private String createFullPath(String path, String name) {
        return path + name + "/";
    }
}