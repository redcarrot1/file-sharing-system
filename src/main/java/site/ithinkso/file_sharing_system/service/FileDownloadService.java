package site.ithinkso.file_sharing_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final FileRepository fileRepository;

    public FileEntity findById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
    }
}
