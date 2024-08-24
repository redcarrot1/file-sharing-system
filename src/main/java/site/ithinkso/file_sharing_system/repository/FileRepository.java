package site.ithinkso.file_sharing_system.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.domain.MetaData;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final MetaDataRepository metaDataRepository;

    public List<FileEntity> saveAll(List<FileEntity> storedFiles) {
        List<MetaData> metaData = metaDataRepository.saveAll(storedFiles);
        return metaData.stream()
                .map(FileEntity.class::cast)
                .toList();
    }

}
