package site.ithinkso.file_sharing_system.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.domain.MetaData;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final MetaDataRepository metaDataRepository;

    public Optional<FileEntity> findById(String id) {
        return metaDataRepository.findById(id, FileEntity.class);
    }

    public List<FileEntity> saveAll(List<FileEntity> storedFiles) {
        List<MetaData> metaData = metaDataRepository.saveAll(storedFiles);
        return metaData.stream()
                .map(FileEntity.class::cast)
                .toList();
    }

    public boolean deleteById(String id) {
        return metaDataRepository.deleteById(id);
    }

    public void updateEntity(FileEntity fileEntity) {
        metaDataRepository.updateEntity(fileEntity);
    }
}
