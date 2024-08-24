package site.ithinkso.file_sharing_system.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.domain.MetaData;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final MongoOperations mongoOps;

    public DirectoryEntity findDirectoryByPath(String directoryPath) {
        Criteria criteria = Criteria.where("directoryFullPath").is(directoryPath);
        Query query = Query.query(criteria);
        return mongoOps.findOne(query, DirectoryEntity.class);
    }

    public void saveAll(List<? extends MetaData> storedFiles) {
        for (MetaData storedFile : storedFiles) {
            mongoOps.insert(storedFile);
        }
    }

    public void updateEntity(MetaData metaData) {
        Criteria criteria = Criteria.where("id").is(metaData.getId());
        Query query = Query.query(criteria);

        mongoOps.findAndReplace(query, metaData);
    }

    public void updateUptoRoot(String startId, long increaseFileCount, long increaseDirectoryCount, long increaseTotalByteSize) {
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
