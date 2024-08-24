package site.ithinkso.file_sharing_system.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DirectoryRepository {

    private final MongoOperations mongoOps;
    private final MetaDataRepository metaDataRepository;

    public DirectoryEntity save(DirectoryEntity storedFile) {
        return (DirectoryEntity) metaDataRepository.save(storedFile);
    }

    public void updateEntity(DirectoryEntity metaData) {
        metaDataRepository.updateEntity(metaData);
    }

    public Optional<DirectoryEntity> findByPath(String directoryPath) {
        Criteria criteria = Criteria.where("directoryFullPath").is(directoryPath);
        Query query = Query.query(criteria);

        DirectoryEntity directory = mongoOps.findOne(query, DirectoryEntity.class);

        return Optional.ofNullable(directory);
    }

    public void updateDirectoryUptoRoot(String startId, long increaseFileCount, long increaseDirectoryCount, long increaseTotalByteSize) {
        UpdateDefinition update = new Update()
                .inc("fileCount", increaseFileCount)
                .inc("directoryCount", increaseDirectoryCount)
                .inc("totalByteSize", increaseTotalByteSize);

        String id = startId;
        while (id != null) {
            Criteria criteria = Criteria.where("id").is(id);
            Query query = Query.query(criteria);

            DirectoryEntity modify = mongoOps.findAndModify(query, update, DirectoryEntity.class);
            if (modify == null) {
                return;
            }

            DirectoryEntity parent = modify.getParent();
            if (parent == null) {
                return;
            }
            id = parent.getId();
        }

    }

}
