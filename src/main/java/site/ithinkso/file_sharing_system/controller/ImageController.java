package site.ithinkso.file_sharing_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.repository.FileRepository;

import java.net.MalformedURLException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final FileRepository fileRepository;

    @GetMapping("/images/preview/{id}")
    public Resource showImage(@PathVariable String id) throws MalformedURLException {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(id);
        if (optionalFileEntity.isEmpty()) {
            return null;
        }
        FileEntity fileEntity = optionalFileEntity.get();

        return new UrlResource("file:" + fileEntity.getThumbnailFullPath());
    }
}
